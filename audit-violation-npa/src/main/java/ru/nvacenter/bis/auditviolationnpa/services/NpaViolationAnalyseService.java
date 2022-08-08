package ru.nvacenter.bis.auditviolationnpa.services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;
import ru.nvacenter.bis.audit.npa.domain.dto.NPANode;
import ru.nvacenter.bis.audit.npa.domain.dto.compare.NPARevisionCompareNode;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_NPAService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_RevisionService;
import ru.nvacenter.bis.audit.npa.services.dto.NPALinearRecursiveService;
import ru.nvacenter.bis.audit.npa.services.dto.NPARevisionCompareService;
import ru.nvacenter.bis.auditviolationcommon.server.domain.*;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.*;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.*;
import ru.nvacenter.bis.auditviolationnpa.models.RepairLink;
import ru.nvacenter.bis.auditviolationnpa.services.dao.DateStructureService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.ListNotActualService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.UniqueStructureService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.ViolationsService;
import ru.nvacenter.bis.auditviolationnpa.services.dto.UniqueCreateStructureService;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by oshesternikova on 19.02.2018.
 * Сервис анализа привязки нарушения к НПА (полнота по редакциям)
 */
@Service
public class NpaViolationAnalyseService {

    @Inject
    UniqueStructureService uniqueStructureService;

    @Inject
    ViolationsService violationsService;

    @Inject
    UniqueCreateStructureService uniqueCreateStructureService;

    @Inject
    DateStructureService dateStructureService;

    @Inject
    FZDocumentService fzDocumentService;

    @Inject
    NVA_SPR_AUD_RevisionService revisionService;

    @Inject
    NPALinearRecursiveService npaLinearRecursiveService;

    @Inject
    NPARevisionCompareService npaRevisionCompareService;

    @Inject
    NVA_SPR_AUD_NPAService nva_SPR_AUD_NPAService;

    private List<AnalyseResult> listResult = new ArrayList<>();

    public List<AnalyseResult> getListResult() {
        return listResult;
    }

    /**
     * Анализ что добавить по всей базе
     */
    public List<AnalyseResult> analyseAll(AnalyseAsyncData data, Optional<Long> idNPA){
        List<NVA_SPR_FKAU_ViolationNPA> ls = null;
        if (idNPA.isPresent())
            ls = violationsService.findAllLinks(idNPA.get());
        else
            ls = violationsService.findAllLinks();
        int index = 1;
        for(NVA_SPR_FKAU_ViolationNPA l : ls){
            data.setStatus(String.format("Обработано нарушений: %d из %d", index++, ls.size()));
            if (data.getIsCanceled())
                break;
            Pair<List<NVA_SPR_FKAU_ViolationNPAStructure>, List<FailedResult>> newValues = create(l);

            if (newValues.getLeft().size() > 0 || newValues.getRight().size() > 0){
                NVA_SPR_AUD_NPA npa = nva_SPR_AUD_NPAService.find(l.getIdSource());
                AnalyseNPAResult npar = convert(npa);
                ViolationData viol = violationsService.find(l.getIdViolation());

                if (newValues.getLeft().size() > 0){
                    for (NVA_SPR_FKAU_ViolationNPAStructure str:
                            newValues.getLeft()) {
                        NVA_SPR_AUD_NPA_Revision rev = null;
                        if (str.getIdRevision() != null)
                            rev = fzDocumentService.findRevisionByID(str.getIdRevision());
                        listResult.add(convert(str, npar, viol, rev));
                    }
                    violationsService.save(newValues.getLeft());
                }

                if (newValues.getRight().size() > 0){

                    for (FailedResult fres : newValues.getRight()) {
                        NVA_SPR_AUD_NPA_Revision rev = fzDocumentService.findRevisionByID(fres.getRev());
                        listResult.add(convert(fres, npar, viol, rev));

                    }
                }
            }

        }

        return listResult;
    }

    /**
     * Анализ что добавить по всей базе (Вариант 2)
     * @param data
     * @param idNPA
     * @return
     */
    public List<AnalyseResult> analyseAll2(AnalyseAsyncData data, Optional<Long> idNPA, Optional<Long> viol) {
        List<NVA_SPR_FKAU_ViolationNPA> ls = null;
        List<AnalyseResult> res = new ArrayList<>();
        if (idNPA.isPresent()){
            if (viol.isPresent())
                ls = violationsService.findAllLinksViolNpa(idNPA.get(), viol.get());
            else
                ls = violationsService.findAllLinks(idNPA.get());
        }
        else {
            if (viol.isPresent())
                ls = violationsService.findAllLinksViol(viol.get());
            else
                ls = violationsService.findAllLinks();
        }

        int index = 1;
        
        Map<Long, ViolationData> cachedViols = new HashMap<>();
        Map<Long, AnalyseNPAResult> cachedNpas = new HashMap<>();
        for (NVA_SPR_FKAU_ViolationNPA l : ls) {
            data.setStatus(String.format("Обработано нарушений: %d из %d", index++, ls.size()));
            if (data.getIsCanceled())
                break;

            if (!cachedViols.containsKey(l.getIdViolation())){
                ViolationData violData = violationsService.find(l.getIdViolation());
                cachedViols.put(l.getIdViolation(), violData);
            }

            if (!cachedNpas.containsKey(l.getIdSource())){
                NVA_SPR_AUD_NPA npa = nva_SPR_AUD_NPAService.find(l.getIdSource());
                AnalyseNPAResult npar = convert(npa);
                cachedNpas.put(l.getIdSource(), npar);
            }
           
            res.addAll(createAndCheck(l, cachedViols.get(l.getIdViolation()), cachedNpas.get(l.getIdSource())));
        }
        return res;
    }

    private AnalyseResult convert(NVA_SPR_FKAU_ViolationNPAStructure str, AnalyseNPAResult npar, ViolationData viol, NVA_SPR_AUD_NPA_Revision rev){
        AnalyseResult r = new AnalyseResult();
        r.setResult("Добавлено");
        r.setText(str.getText());
        r.setDATE_END(str.getDateEnd());
        r.setDATE_BEGIN(str.getDateBegin());
        r.setViolText(viol.getViolationText());
        r.setRev(rev);
        r.setNpa(npar);
        r.setIdLink(str.getId());
        return r;
    }

    private AnalyseResult convert(FailedResult str, AnalyseNPAResult npar, ViolationData viol, NVA_SPR_AUD_NPA_Revision rev){
        AnalyseResult r = new AnalyseResult();
        r.setResult(str.getResult());
        r.setText(str.getText());
        r.setViolText(viol.getViolationText());
        r.setRev(rev);
        r.setNpa(npar);
        r.setIdLink(str.getId());
        return r;
    }

    private AnalyseNPAResult convert(NVA_SPR_AUD_NPA npa){
        AnalyseNPAResult npar = new AnalyseNPAResult();
        if(npa == null) {
            npar.setNum(StringUtils.EMPTY);
            npar.setText("ДОКУМЕНТ УДАЛЕН");
            npar.setDate(LocalDate.of(1,1,1));
        }
        else{
            npar.setNum(npa.getNumber());
            npar.setText(npa.getName());
            npar.setDate(npa.getDate());
            npar.setDeleted(npa.getDeleted());
        }

        return npar;
    }

    /**
     * Создать недостающие ссылки
     * @param link
     * @return Новые значения + значения ошибок
     */
    private Pair<List<NVA_SPR_FKAU_ViolationNPAStructure>, List<FailedResult>> create(NVA_SPR_FKAU_ViolationNPA link){
        List<NVA_SPR_FKAU_ViolationNPAStructure> res = new ArrayList<>();
        List<NVA_SPR_FKAU_ViolationNPAStructure> arr = new ArrayList<>();
        List<FailedResult> failed = new ArrayList<>();
        arr.addAll(link.getStructures());
        //Идем по ссылкам нарушения
        for(int i =0; i < arr.size(); i++){
            NVA_SPR_FKAU_ViolationNPAStructure tr = arr.get(i);
            String strId = tr.getIdSource();
            //Привязано не к структурной единице
            if (strId == null) continue;
            Optional<Long> revId = tr.getIdRevision() == null ? Optional.empty() : Optional.of(tr.getIdRevision());
            Long docId = link.getIdSource();
            //Создаем "уникальные" структурные единицы
            List<FailedResult> f = uniqueCreateStructureService.createUniqueStructureElement(strId, revId, docId);
            //Работа с "неудачами"
            for (FailedResult ff : f) {
                ff.setText(tr.getText());
                ff.setId(tr.getId());
            }
            failed.addAll(f);
            //Находим "уникальную" структурную единицу
            if (!revId.isPresent()){
                Long tryRev = uniqueStructureService.tryFindRevision(docId, strId);
                if (tryRev != null) revId = Optional.of(tryRev);
            }
            UniqueStructureElement el = uniqueStructureService.find(strId, revId, docId);
            if (el == null) continue;
            //Массив "реальных" структурных единиц, привязанных к "уникальной"
            Set<UniqueRealStructureElement> rs = el.getReals();
            if (rs == null) continue;
            for(UniqueRealStructureElement r : rs){
                //Те, которые под вопросом
                if (r.getIdRealStructure() == null) continue;
                //Проверяем, обработаны ли уже ???
                NVA_SPR_FKAU_ViolationNPAStructure forRemove = null;
                for(int j = i; j < arr.size();j++)
                {
                    if (compare(r, arr.get(j))){
                        forRemove = arr.get(j);
                        break;
                    }
                }
                if (forRemove != null)
                    arr.remove(forRemove);
                else{
                    //Создаем ссылку
                    NVA_SPR_FKAU_ViolationNPAStructure st = convert(el, r, strId);
                    st.setIdNPA(link.getId());
                    st.setText(tr.getText());
                    Optional<Long> revId2 = r.getIdRev() == null ? Optional.empty() : Optional.of(r.getIdRev());
                    //Находим дату действия ссылки (рекурсивно или из редакции, если не нашли)
                    Pair<LocalDate, LocalDate> dates = dateStructureService.findDateStartAndEnd(link.getId(), revId2, r.getIdRealStructure());
                    st.setDateBegin(dates.getLeft());
                    st.setDateEnd(dates.getRight());
                    res.add(st);
                }

            }
        }
        return Pair.of(res, failed);
    }

    /**
     * Создать недостающие ссылки, проверить и исправить существующие
     * @param link
     * @return Новые значения + значения ошибок
     */
    private List<AnalyseResult> createAndCheck(NVA_SPR_FKAU_ViolationNPA link, ViolationData vd, AnalyseNPAResult npar){
        Set<NVA_SPR_FKAU_ViolationNPAStructure> arr = link.getStructures();
        Long docId = link.getIdSource();
        //Группируем по исходной ссылке
        Map<String, List<NVA_SPR_FKAU_ViolationNPAStructure>> map =  arr.stream()
                .filter(a -> a.getIdSource() != null)
                .collect(Collectors.groupingBy(a -> a.getIdSource(), Collectors.toList()));

        List<NVA_SPR_FKAU_ViolationNPAStructure> changed = new ArrayList<>();
        List<AnalyseResult> res = new ArrayList<>();
        //Проходим для каждой группы ссылок
        for (Map.Entry<String, List<NVA_SPR_FKAU_ViolationNPAStructure>> ent :
                map.entrySet()) {
            changed.addAll(createAndCheck(docId, ent.getValue(), link.getId()));
        }

        List<NVA_SPR_AUD_NPA_Revision> allRevs = fzDocumentService.findRevisionByNpa(docId);
        for (NVA_SPR_FKAU_ViolationNPAStructure str:
             changed) {
            NVA_SPR_AUD_NPA_Revision rr = allRevs.stream()
                    .filter(r -> longcompare(r.getId(), str.getIdRevision()))
                    .findFirst()
                    .orElse(null);
            res.add(convert(str, vd, npar, rr));
        }
        violationsService.save(changed);
        return res;
    }

    public List<NVA_SPR_FKAU_ViolationNPAStructure> createAndCheck(Long docId, List<NVA_SPR_FKAU_ViolationNPAStructure> arr, Long linkId) {
        List<NVA_SPR_FKAU_ViolationNPAStructure> changed = new ArrayList<>();
        Optional<NVA_SPR_FKAU_ViolationNPAStructure> o = arr
                .stream()
                .filter(v -> v.getIdRevision() != null)
                .findFirst();
        if (o.isPresent()){
            String strId = o.get().getIdRevisionStructure() == null ? o.get().getIdSource() : o.get().getIdRevisionStructure();
            Long revId = o.get().getIdRevision();
            //Создаем классы эквивалентности
            uniqueCreateStructureService.createOrUpdateUniqueElements(strId, revId, docId);
            //Достаем классы эквивалетности
            Map<UniqueStructureElement, List<UniqueRealStructureElement>> maps = uniqueStructureService.findEquivalentClasses(strId, Optional.of(revId), docId);

            //Проверяем уже существующие
            for (NVA_SPR_FKAU_ViolationNPAStructure vstr :
                    arr) {
                boolean hasSame = false;
                for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> eq :
                        maps.entrySet()) {
                    hasSame = eq.getValue()
                            .stream()
                            .anyMatch(e -> StringUtils.equals(e.getIdRealStructure(), strId) && longcompare(e.getIdRev(), revId));
                    if (hasSame){
                        //Проверяем нужно ли править даты (?)
                        String strId2 = vstr.getIdRevisionStructure() == null ? vstr.getIdSource() : vstr.getIdRevisionStructure();
                        Pair<LocalDate, LocalDate> dates = dateStructureService.findDateStartAndEnd(docId, Optional.of(vstr.getIdRevision()), strId2);
                        if (!localdatecompare(vstr.getDateBegin(), dates.getLeft()) || !localdatecompare(vstr.getDateEnd(), dates.getRight())){
                            vstr.setDateBegin(dates.getLeft());
                            vstr.setDateEnd(dates.getRight());
                            changed.add(vstr);
                        }

                        break;
                    }
                }
                if (!hasSame){
                    vstr.setDeleted(false);
                    changed.add(vstr);//если нигде не нашли -на удаление
                }
            }

            //Ищем новые добавленные
            NVA_SPR_FKAU_ViolationNPAStructure proto = arr.get(0);

            for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> eq :
                    maps.entrySet()){
                boolean isFailed = eq.getValue()
                        .stream()
                        .allMatch(e -> e.getIdRealStructure() == null);
                if (isFailed) continue;
                List<UniqueRealStructureElement> linked = new ArrayList<>();
                List<UniqueRealStructureElement> notlinked = new ArrayList<>();
                for (UniqueRealStructureElement urse :
                        eq.getValue()) {

                    boolean hasSame = false;
                    for (NVA_SPR_FKAU_ViolationNPAStructure vstr :
                            arr){
                        String strEl = vstr.getIdRevisionStructure() == null ? vstr.getIdSource() : vstr.getIdRevisionStructure();
                        hasSame = StringUtils.equals(strEl, urse.getIdRealStructure()) && longcompare(vstr.getIdRevision(), urse.getIdRev());
                        if (hasSame) break;
                    }
                    if (hasSame)
                        linked.add(urse);
                    else
                        notlinked.add(urse);
                }

                if (linked.size() > 0 &&  notlinked.size() > 0){
                    for (UniqueRealStructureElement urse :
                            notlinked) {
                        //Создаем ссылку

                        NVA_SPR_FKAU_ViolationNPAStructure st = createLink(urse, proto);
                        Optional<Long> revId2 = urse.getIdRev() == null ? Optional.empty() : Optional.of(urse.getIdRev());
                        //Находим дату действия ссылки (рекурсивно или из редакции, если не нашли)
                        Pair<LocalDate, LocalDate> dates = dateStructureService.findDateStartAndEnd(linkId, revId2, urse.getIdRealStructure());
                        st.setDateBegin(dates.getLeft());
                        st.setDateEnd(dates.getRight());
                        changed.add(st);
                    }
                }
            }
        }
        return changed;
    }

    private AnalyseResult convert(NVA_SPR_FKAU_ViolationNPAStructure str, ViolationData vd, AnalyseNPAResult npar, NVA_SPR_AUD_NPA_Revision rev){
        AnalyseResult r = new AnalyseResult();
        String strRes = null;
        if (str.getId() == null)
            strRes = "Добавлено";
        else if (str.isDeleted())
            strRes = "Удалено";
        else
            strRes = "Изменена ссылка на положение НПА";
        r.setResult(strRes);
        r.setText(str.getText());
        r.setViolText(vd.getViolationText());
        r.setRev(rev);
        r.setNpa(npar);
        r.setIdLink(str.getId());
        r.setDATE_END(str.getDateEnd());
        r.setDATE_BEGIN(str.getDateBegin());
        return r;
    }

    /**
     * Создать группировки по тексту (для конкретного НПА)
     * @param npaId НПА
     */
    public void createUniqueElementsForViolLinks(Long npaId){
        List<NVA_SPR_FKAU_ViolationNPA> npaLinks = violationsService.findAllLinks(npaId);
        for (NVA_SPR_FKAU_ViolationNPA n :
                npaLinks) {
            List<NVA_SPR_FKAU_ViolationNPAStructure> strs = violationsService.findAllLinksStr(n.getId());
            Map<String, List<NVA_SPR_FKAU_ViolationNPAStructure>> maps = strs.stream()
                    .collect(Collectors.groupingBy(s -> s.getIdSource()));
            for (Map.Entry<String, List<NVA_SPR_FKAU_ViolationNPAStructure>> m :
                    maps.entrySet()) {
                NVA_SPR_FKAU_ViolationNPAStructure link = m.getValue().get(0);
                String strId = link.getIdRevisionStructure() == null ? link.getIdSource() : link.getIdRevisionStructure();
                uniqueCreateStructureService.createOrUpdateUniqueElements(strId, link.getIdRevision(), npaId);
            }
        }
    }

    private static boolean compare(UniqueRealStructureElement r, NVA_SPR_FKAU_ViolationNPAStructure t){
        String strId = t.getIdRevisionStructure() == null ? t.getIdSource() : t.getIdRevisionStructure();
        return StringUtils.equals(r.getIdRealStructure(), strId) &&
                (r.getIdRev() == null && t.getIdRevision() == null || (t.getIdRevision() != null && r.getIdRev().equals(t.getIdRevision())));
    }

    private static NVA_SPR_FKAU_ViolationNPAStructure convert(UniqueStructureElement el, UniqueRealStructureElement r, String idReal){
        NVA_SPR_FKAU_ViolationNPAStructure tr = new NVA_SPR_FKAU_ViolationNPAStructure();
        tr.setIdRevision(r.getIdRev());
        tr.setIdRevisionStructure(r.getIdRealStructure());
        tr.setIdSource(idReal);
        return  tr;
    }

    private NVA_SPR_FKAU_ViolationNPAStructure createLink(UniqueRealStructureElement r, NVA_SPR_FKAU_ViolationNPAStructure proto){
        NVA_SPR_FKAU_ViolationNPAStructure tr = new NVA_SPR_FKAU_ViolationNPAStructure();
        tr.setIdRevision(r.getIdRev());
        tr.setIdRevisionStructure(r.getIdRealStructure());
        tr.setIdSource(proto.getIdSource());
        tr.setText(proto.getText());
        tr.setIdNPA(proto.getIdNPA());
        return  tr;
    }

    /**
     * Случаи нераспознанных ссылок
     * @return
     */
    public List<AnalyseResultBase> findDoubtful(){
        List<UniqueRealStructureElement> emps = uniqueStructureService.findEmptyLinks();
        List<Long> els = emps.stream()
                .map(e -> e.getIdUniqueStructure())
                .distinct()
                .collect(Collectors.toList());
        List<Long> revs = emps.stream()
                .map(e -> e.getIdRev())
                .distinct()
                .filter(e -> e != null)
                .collect(Collectors.toList());

        List<UniqueStructureElement> uels = uniqueStructureService.find(els);

        List<Long> npas = uels.stream()
                .map(e -> e.getNpa())
                .distinct()
                .collect(Collectors.toList());

        List<NVA_SPR_AUD_NPA> allNPAs = nva_SPR_AUD_NPAService.find(npas);
        List<NVA_SPR_AUD_NPA_Revision> allRevs = revisionService.find(revs);

        List<AnalyseResultBase> res = new ArrayList<>();
        for(UniqueRealStructureElement el : emps){
            AnalyseResultBase ar = new AnalyseResultBase();
            Long n = el.getIdUniqueStructure();
            UniqueStructureElement uel = uels.stream()
                    .filter(u ->  u.getId().equals(n))
                    .findFirst()
                    .orElse(null);

            ar.setText(uel.getName());
            NVA_SPR_AUD_NPA npa = allNPAs.stream()
                    .filter(a -> a.getId().equals(uel.getNpa()))
                    .findFirst()
                    .orElse(null);
            ar.setNpa(convert(npa));
            NVA_SPR_AUD_NPA_Revision rev = allRevs.stream()
                    .filter(a -> a.getId().equals(el.getIdRev()))
                    .findFirst()
                    .orElse(null);
            ar.setRev(rev);
            res.add(ar);
        }
        return res;
    }

    /**
     * Исправить ссылку
     * @param id ID ссылки
     * @return
     */
    public boolean repair(Long id){
        NVA_SPR_FKAU_ViolationNPAStructure link = violationsService.findLink(id);
        NVA_SPR_FKAU_ViolationNPA lnpa = violationsService.findLinkNpa(link.getIdNPA());
        Long idRev = uniqueStructureService.tryFindRevision(lnpa.getIdSource(), link.getIdSource());
        if (idRev != null){
            link = fillLink(link, lnpa, idRev);
            List<NVA_SPR_FKAU_ViolationNPAStructure> strs = new ArrayList<>();
            strs.add(link);
            violationsService.save(strs);
            return true;
        }
        return false;
    }

    public List<RepairLink> repairAll(List<RepairLink> ids) {
        List<NVA_SPR_FKAU_ViolationNPAStructure> listViolStruct = new ArrayList<>();
        List<RepairLink> notRepairLinks = new ArrayList<>();
        for(RepairLink id : ids) {
            NVA_SPR_FKAU_ViolationNPAStructure link = violationsService.findLink(id.getId());
            if(link != null) {
                NVA_SPR_FKAU_ViolationNPA lnpa = violationsService.findLinkNpa(link.getIdNPA());
                if(id.getIdRev() != null) {
                    link = fillLink(link, lnpa, id.getIdRev());
                    listViolStruct.add(link);
                }
                else {
                    notRepairLinks.add(id);
                }
            }
            else {
                notRepairLinks.add(id);
            }
        }
        violationsService.save(listViolStruct);
        return notRepairLinks;
    }

    private NVA_SPR_FKAU_ViolationNPAStructure fillLink(NVA_SPR_FKAU_ViolationNPAStructure link, NVA_SPR_FKAU_ViolationNPA lnpa, Long idRev) {
        link.setIdRevision(idRev);
        link.setIdRevisionStructure(link.getIdSource());
        Pair<LocalDate, LocalDate> dts = dateStructureService.findDateStartAndEnd(lnpa.getIdSource(), Optional.of(idRev), link.getIdSource());
        link.setDateBegin(dts.getLeft());
        link.setDateEnd(dts.getRight());
        return link;
    }

    public NPARevisionCompareNode compareRevAbstract(Long idLink, Long revId1, Long revId2){
        NVA_SPR_FKAU_ViolationNPAStructure str = violationsService.findLink(idLink);
        NVA_SPR_FKAU_ViolationNPA lnpa = violationsService.findLinkNpa(str.getIdNPA());

        Long idNpa = lnpa.getIdSource();
        String idStr = str.getIdRevisionStructure() == null ? str.getIdSource() : str.getIdRevisionStructure();
        Long idRev = str.getIdRevision();
        if (idRev == null){
            idRev = uniqueStructureService.tryFindRevision(idNpa, idStr);
        }
        Pair<String, String> p = findElements(idNpa, idStr, idRev, revId1, revId2);
        if (p.getLeft() == null || p.getRight() == null) return null;

        List<NVA_SPR_AUD_NPA_STRUCTURE> str1 = fzDocumentService.findAbstract(p.getLeft(), revId1, idNpa);
        List<NVA_SPR_AUD_NPA_STRUCTURE> str2 = fzDocumentService.findAbstract(p.getRight(), revId2, idNpa);

        NPANode rev1 = npaLinearRecursiveService.ToRecursive(str1);
        NPANode rev2 = npaLinearRecursiveService.ToRecursive(str2);
        NPARevisionCompareNode compare =  npaRevisionCompareService.mergeRoot(rev1, rev2, false);
        compare.setHasDiffs(compare.hasAnyChanges());
        return compare;
        //return npaRevisionCompareService.filterCompareNode(compare);
    }

    private Pair<String, String> findElements(Long idNpa,String idStr, Long idRev, Long revId1, Long revId2){

        Map<UniqueStructureElement, List<UniqueRealStructureElement>> maps = uniqueStructureService.findEquivalentClasses(idStr, idRev == null ? Optional.empty() : Optional.of(idRev), idNpa);

        String strId1 = null;
        String strId2 = null;
        for(Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> m : maps.entrySet()){
            Optional<UniqueRealStructureElement> r1 = m.getValue()
                    .stream()
                    .filter(ff -> longcompare(ff.getIdRev(), revId1) && ff.getIdRealStructure() != null)
                    .findFirst();

            if (r1.isPresent())
                strId1 = r1.get().getIdRealStructure();

            Optional<UniqueRealStructureElement> r2 = m.getValue()
                    .stream()
                    .filter(ff -> longcompare(ff.getIdRev(), revId2) && ff.getIdRealStructure() != null)
                    .findFirst();

            if (r2.isPresent())
                strId2 = r2.get().getIdRealStructure();

        }
        return Pair.of(strId1, strId2);
    }

    private static boolean longcompare(Long l1, Long l2){
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        return Long.compare(l1, l2) == 0;
    }

    private static boolean localdatecompare(LocalDate d1, LocalDate d2){
        if (d1 == null && d2 == null) return true;
        if (d1 == null || d2 == null) return false;
        return d1.isEqual(d2);
    }
}
