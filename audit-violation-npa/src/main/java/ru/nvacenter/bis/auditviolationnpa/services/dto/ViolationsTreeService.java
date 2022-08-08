package ru.nvacenter.bis.auditviolationnpa.services.dto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;
import ru.nvacenter.bis.audit.npa.domain.dto.NPA_AUD_ContentNode;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_NPAService;
import ru.nvacenter.bis.audit.npa.services.dto.NPALinearRecursiveService;
import ru.nvacenter.bis.auditviolationcommon.server.domain.*;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.ListNotActual;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.UniqueRealStructureElement;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.UniqueStructureElement;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.*;
import ru.nvacenter.bis.auditviolationnpa.services.dao.*;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by oshesternikova on 19.03.2018.
 * Нарушения как дерево
 */
@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="jpaTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class ViolationsTreeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FZDocumentService.class);
    private static final String ERROR_MESSAGE = "Ошибка при выполнении запроса '%s'";


    @Inject
    private ViolationsService violationsService;
    @Inject
    FZDocumentService fzDocumentService;
    @Inject
    UniqueStructureService uniqueStructureService;
    @Inject
    NPALinearRecursiveService npaLinearRecursiveService;
    @Inject
    ListNotActualService listNotActualService;
    @Inject
    NVA_SPR_AUD_NPAService nva_SPR_AUD_NPAService;
    /**
     * Список всех нарушений
     * @return
     */
    public Collection<ViolationTreeData> findViolationsAsTree(){
        List<NVA_SPR_FKAU_ViolationNPAStructure> strs = violationsService.findAllLinksStr();
        Map<Long, List<NVA_SPR_FKAU_ViolationNPAStructure>> groups = strs.stream()
                .collect(Collectors.groupingBy(NVA_SPR_FKAU_ViolationNPAStructure::getIdNPA));

        List<NVA_SPR_FKAU_ViolationNPA> links = violationsService.findAllLinks();
        List<NVA_SPR_FKAU_ViolationNPA> used = new ArrayList<>();
        Map<Long, ViolationTreeData> vdatas = new HashMap<>();
        List<NVA_SPR_AUD_NPA> delNpas = nva_SPR_AUD_NPAService.findDeleted();

        Map<Long, List<NVA_SPR_AUD_NPA_Revision>> mapRevisions = new HashMap<>();
        for(Map.Entry<Long, List<NVA_SPR_FKAU_ViolationNPAStructure>> entry : groups.entrySet()){

            NVA_SPR_FKAU_ViolationNPA lnpa = links.stream()
                    .filter(l -> Long.compare(l.getId(), entry.getKey()) == 0)
                    .findFirst()
                    .orElse(null);
            if (lnpa == null) continue;
            used.add(lnpa);
            if (!vdatas.containsKey(lnpa.getIdViolation())){
                ViolationData vd = violationsService.find(lnpa.getIdViolation());
                vdatas.put(lnpa.getIdViolation(), new ViolationTreeData(vd));
            }
            //Все редакции документа
            List<NVA_SPR_AUD_NPA_Revision> revs = null;
            if (!mapRevisions.containsKey(lnpa.getIdSource())){
                revs = fzDocumentService.findRevisionByNpa(lnpa.getIdSource());
                mapRevisions.put(lnpa.getIdSource(), revs);
            }
            else
                revs = mapRevisions.get(lnpa.getIdSource());


            ViolationTreeData vtd = vdatas.get(lnpa.getIdViolation());
            Map<String, List<NVA_SPR_FKAU_ViolationNPAStructure>> llinks = entry.getValue().stream()
                    .collect(Collectors.groupingBy(ss1 -> ss1.getIdSource() == null ? StringUtils.EMPTY : ss1.getIdSource()));
            for(Map.Entry<String, List<NVA_SPR_FKAU_ViolationNPAStructure>> ll : llinks.entrySet()){
                ViolationLinkData ld = createLink(ll, revs, lnpa, delNpas);
                vtd.getLinks().add(ld);
            }
        }
        for (NVA_SPR_FKAU_ViolationNPA ln:
             used) {
            links.remove(ln);
        }

        Map<Long, List<NVA_SPR_FKAU_ViolationNPA>> addLinks = links.stream().collect(Collectors.groupingBy(NVA_SPR_FKAU_ViolationNPA::getIdViolation));
        for (ViolationTreeData vtd:
                vdatas.values()) {
            if (addLinks.containsKey(vtd.getId())){
                for (NVA_SPR_FKAU_ViolationNPA vn:
                        addLinks.get(vtd.getId())) {
                    ViolationLinkData ld = createLink(vn, delNpas);
                    vtd.getLinks().add(ld);
                }
            }
        }


        return vdatas.values()
                .stream()
                .sorted((v1, v2) -> StringUtils.compare(v1.getText(), v2.getText()))
                .collect(Collectors.toList());
    }

    /**
     * Редакции
     * @param idNpaLink
     * @return
     */
    public List<ViolationRevData> revisionsByLink(Long idNpaLink) {
        List<ViolationRevData> arr = new ArrayList<>();
        NVA_SPR_FKAU_ViolationNPAStructure str = violationsService.findLink(idNpaLink);
        if (str != null)
        {
            List<NVA_SPR_FKAU_ViolationNPAStructure> strs = violationsService.findAllLinksStr(str);
            NVA_SPR_FKAU_ViolationNPA lnpa = violationsService.findLinkNpa(str.getIdNPA());
            List<Long> revsOk = strs.stream().map(ss -> ss.getIdRevision()).collect(Collectors.toList());
            String strIds = str.getIdRevisionStructure() == null ? str.getIdSource() : str.getIdRevisionStructure();
            List<ListNotActual> notActuals = listNotActualService.find(lnpa.getId());
            final LocalDate dtDef = lnpa.getDate();
            //Все редакции
            List<NVA_SPR_AUD_NPA_Revision> revs = fzDocumentService.findRevisionByNpa(lnpa.getIdSource());
            if (fzDocumentService.checkNullRevision(lnpa.getIdSource())){
                revs.add(null);
            }
            Long idRev = str.getIdRevision();
            if (idRev == null){
                idRev = uniqueStructureService.tryFindRevision(lnpa.getIdSource(), str.getIdSource());
            }
            //Редакции, которые есть
            Map<UniqueStructureElement, List<UniqueRealStructureElement>> maps = uniqueStructureService.findEquivalentClasses(strIds, idRev == null ? Optional.empty() : Optional.of(idRev), lnpa.getIdSource());
            revs.sort(new Comparator<NVA_SPR_AUD_NPA_Revision>() {
                @Override
                public int compare(NVA_SPR_AUD_NPA_Revision o1, NVA_SPR_AUD_NPA_Revision o2) {
                    if (o1 == null && o2 == null) return 0;
                    if (o1 == null) return 1;
                    if (o2 == null) return -1;
                    return o1.getDtRev().compareTo(o2.getDtRev());
                }
            } );
            UniqueStructureElement currentUq = null;
            List<NVA_SPR_AUD_NPA_Revision> listRevisions = new ArrayList<>();
            for(NVA_SPR_AUD_NPA_Revision rev :revs ) {
                UniqueStructureElement uq = null;
                for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> m :
                        maps.entrySet()) {
                    Optional<UniqueRealStructureElement> el = m.getValue()
                            .stream()
                            .filter(g -> rev != null ? longcompare(g.getIdRev(), rev.getId()) : longcompare(g.getIdRev(), null))
                            .findFirst();
                    if (el.isPresent()){
                        uq = m.getKey();
                        break;
                    }
                }
                if (uq != currentUq){
                    if (listRevisions.size() > 0){
                        ViolationRevData vrd = createDataFromRevList(listRevisions, dtDef, currentUq, maps.get(currentUq), revsOk, notActuals);
                        arr.add(vrd);
                        listRevisions.clear();
                    }
                }
                listRevisions.add(rev);
                currentUq = uq;
            }
            if (listRevisions.size() > 0 && !(listRevisions.size() == 1 && listRevisions.get(0) == null)){
                ViolationRevData vrd = createDataFromRevList(listRevisions, dtDef, currentUq, maps.get(currentUq), revsOk, notActuals);
                arr.add(vrd);
            }
            arr.sort((c1, c2) ->  c2.getDtStart().compareTo(c1.getDtStart()));
        }
        return arr;
    }

    private void checkListRevsSize()
    {

    }

    private ViolationRevData createDataFromRevList(List<NVA_SPR_AUD_NPA_Revision> listRevisions, LocalDate dtDef, UniqueStructureElement currentUq, List<UniqueRealStructureElement> currentListReals,  List<Long> revsOk,  List<ListNotActual> revsNot){
        LocalDate dt1 = listRevisions.get(0) == null ? dtDef : listRevisions.get(0).getDtBegin();
        LocalDate dt2 = listRevisions.get(listRevisions.size() - 1) == null ? null : listRevisions.get(listRevisions.size() - 1).getDtEnd();

        ViolationRevData.ViolRevState st = ViolationRevData.ViolRevState.NOT_KNOWN;
        ViolationRevData.StructElemRevState structElemRevState = ViolationRevData.StructElemRevState.NONE;
        if (currentUq != null){
            boolean allEmpty = currentListReals
                    .stream()
                    .allMatch(mm -> mm.getIdRealStructure() == null);
            if (allEmpty){
                st = ViolationRevData.ViolRevState.NO;
                structElemRevState = ViolationRevData.StructElemRevState.NOT_FOUND;
            }
            else{
                boolean b1 =  currentListReals
                        .stream()
                        .allMatch(mm -> revsOk.contains(mm.getIdRev()));
                if (b1)
                    st = ViolationRevData.ViolRevState.YES;
                else{
                    boolean not = true;
                    for (UniqueRealStructureElement lna:
                            currentListReals) {
                        boolean b2 = revsNot
                                .stream()
                                .anyMatch(a -> longcompare(a.getListNotActualPK().getIdRev(), lna.getIdRev()) &&
                                StringUtils.equals(a.getListNotActualPK().getIdSource(), lna.getIdRealStructure()));
                        if (!b2){
                            not = false;
                            break;
                        }
                    }
                    if (not)
                        st = ViolationRevData.ViolRevState.NO;
                    //else
                    //    structElemRevState = ViolationRevData.StructElemRevState.NOT_ANALYSED;
                }


            }

        }
        else
            structElemRevState = ViolationRevData.StructElemRevState.NOT_ANALYSED;

        ViolationRevData vrd = new ViolationRevData(dt1, dt2, st, structElemRevState);
        for (NVA_SPR_AUD_NPA_Revision r:
                listRevisions) {
            if (r != null)
                vrd.getRevisions().add(r);
        }
        return vrd;
    }

    private static boolean longcompare(Long l1, Long l2){
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        return Long.compare(l1, l2) == 0;
    }

    public List<NPA_AUD_ContentNode> structureAbstractByLink(Long idNpaLink, Long idRev) {
        NVA_SPR_FKAU_ViolationNPAStructure str = violationsService.findLink(idNpaLink);
        NVA_SPR_FKAU_ViolationNPA lnpa = violationsService.findLinkNpa(str.getIdNPA());
        Long idRevReal = str.getIdRevision();
        String strId = str.getIdRevisionStructure() == null ? str.getIdSource() : str.getIdRevisionStructure();
        String strEl = null;
        if (longcompare(idRevReal, idRev)){
            strEl = strId;
        }
        else{
            if (idRevReal == null)
                idRevReal = uniqueStructureService.tryFindRevision(lnpa.getIdSource(), strId);
            Map<UniqueStructureElement, List<UniqueRealStructureElement>> maps = uniqueStructureService.findEquivalentClasses(strId, idRevReal == null ? Optional.empty() : Optional.of(idRevReal), lnpa.getIdSource());
            for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> m :
                    maps.entrySet()) {
                UniqueRealStructureElement el = m.getValue().stream()
                        .filter(a -> longcompare(a.getIdRev(), idRev))
                        .findFirst()
                        .orElse(null);
                if (el != null){
                    strEl = el.getIdRealStructure();
                    break;
                }
            }
        }
        if (strEl == null) return new ArrayList<>();
        return findAbstract(strEl, idRev, lnpa.getIdSource());
    }

    public List<NPA_AUD_ContentNode> structureAbstractByLink(Long idNpaLink) {
        NVA_SPR_FKAU_ViolationNPAStructure str = violationsService.findLink(idNpaLink);
        NVA_SPR_FKAU_ViolationNPA lnpa = violationsService.findLinkNpa(str.getIdNPA());
        Long idRev = str.getIdRevision();

        if (idRev == null){
            idRev = uniqueStructureService.tryFindRevision(lnpa.getIdSource(), str.getIdSource());
        }

        return findAbstract(str.getIdSource(), idRev, lnpa.getIdSource());
    }

    private List<NPA_AUD_ContentNode> findIfDeleted(String idStr, Long idRev, Long idNpa){
        List<NPA_AUD_ContentNode> lst = new ArrayList<>();
        boolean b = fzDocumentService.checkIfDeleted(idStr, idRev, idNpa);
        if (b){
            lst.add(new NPA_AUD_ContentNode("[Удалено]"));
        }
        return lst;
    }

    private List<NPA_AUD_ContentNode> findAbstract(String idStr, Long idRev, Long idNpa){
        boolean b = fzDocumentService.checkHasChildren(idNpa, idRev, idStr);
        if (b){
            //Есть смысл вытаскивать все
            List<NVA_SPR_AUD_NPA_STRUCTURE> lst = fzDocumentService.getListStructure(idNpa.toString(), idRev == null ? Optional.of(0L) : Optional.of(idRev), Optional.empty());
            List<NPA_AUD_ContentNode> data = npaLinearRecursiveService.ToRecursiveViewFromPoint(lst, idStr);
            return data;
        }
        NVA_SPR_AUD_NPA_STRUCTURE strel = fzDocumentService.getStructureElement(idNpa, idRev, idStr);
        List<NPA_AUD_ContentNode> lst = new ArrayList<>();
        if (strel == null)
            return findIfDeleted(idStr, idRev, idNpa);
        lst.add(npaLinearRecursiveService.createCont(strel, false));
        return lst;
    }

    /***
     * Найти список нарушений, в которых есть неопределнности о положениях
     * @return
     */
    public Collection<ViolationTreeData> findDoubtfulViols(){
        List<NVA_SPR_FKAU_ViolationNPAStructure> strs = violationsService.findAllLinksStr();
        Map<Long, List<NVA_SPR_FKAU_ViolationNPAStructure>> groups = strs.stream()
                .collect(Collectors.groupingBy(NVA_SPR_FKAU_ViolationNPAStructure::getIdNPA));
        //Кэш для данных по нарушениям
        Map<Long, ViolationData> violationsCache = new HashMap<>();
        //Кэш для данных по нарушениям
        Map<Long, ViolationTreeData> violationTreeCache = new HashMap<>();
        //Кэш для данных по НПА
        Map<Long, List<NVA_SPR_AUD_NPA_Revision>> npaRevsCache = new HashMap<>();
        List<UniqueRealStructureElement> allLinks = uniqueStructureService.findAllLinks();
        List<UniqueStructureElement> allUniques = uniqueStructureService.findAllUniques();
        Map<Long, List<UniqueRealStructureElement>> grRealLinks = allLinks.stream()
                .collect(Collectors.groupingBy(UniqueRealStructureElement::getIdUniqueStructure));
        Map<UniqueStructureElement.NpaTextName, List<UniqueStructureElement>> grUniqueLinks = allUniques.stream()
                .collect(Collectors.groupingBy(UniqueStructureElement::getGroupping));
        List<NVA_SPR_AUD_NPA> delNpas = nva_SPR_AUD_NPAService.findDeleted();

        //Идем по всем ссылкам на НПА
        for (Map.Entry<Long, List<NVA_SPR_FKAU_ViolationNPAStructure>> ent:
             groups.entrySet()) {
            Long idNpa = ent.getKey();
            NVA_SPR_FKAU_ViolationNPA vnpa = violationsService.findLinkNpa(idNpa);
            //Заполняем кеш по нарушениям по необходимости
            if (!violationsCache.containsKey(vnpa.getIdViolation())){
                ViolationData vdata = violationsService.find(vnpa.getIdViolation());
                violationsCache.put(vdata.getId(), vdata);
                violationTreeCache.put(vdata.getId(), new ViolationTreeData(vdata));
            }
            //Ссылки, определенные как "Нет"
            List<ListNotActual> lnacts = listNotActualService.find(idNpa);
            //Ссылки, определенные как "Да"
            List<NVA_SPR_FKAU_ViolationNPAStructure> lacts = ent.getValue();
            if (!npaRevsCache.containsKey(vnpa.getIdSource())){
                List<NVA_SPR_AUD_NPA_Revision> revs = fzDocumentService.findRevisionByNpa(vnpa.getIdSource());
                npaRevsCache.put(vnpa.getIdSource(), revs);
            }

            ViolationTreeData current = violationTreeCache.get(vnpa.getIdViolation());
            //Группируем по пункту
            Map<String, List<NVA_SPR_FKAU_ViolationNPAStructure>> m2 = lacts.stream().collect(Collectors.groupingBy(NVA_SPR_FKAU_ViolationNPAStructure::getText));
            //Идем по всем положениям
            for (Map.Entry<String, List<NVA_SPR_FKAU_ViolationNPAStructure>> mm:
                 m2.entrySet()) {
               //Создаем ссылку для нарушения (второй уровень дерева)
                ViolationLinkData vld = createLink(mm, npaRevsCache.get(vnpa.getIdSource()), vnpa, delNpas);
                //Проверяем статус
                boolean st = false;
                NVA_SPR_FKAU_ViolationNPAStructure strAny = mm.getValue().get(0);
                Optional<UniqueRealStructureElement> curr =  allLinks.stream()
                        .filter(f -> longcompare(f.getIdRev(), strAny.getIdRevision()) && StringUtils.equals(f.getIdRealStructure(), strAny.getIdRevisionStructure()))
                        .findFirst();
                if (curr.isPresent()){

                    List<UniqueRealStructureElement> rsels = new ArrayList<>(); //grRealLinks.get(curr.get().getIdUniqueStructure());
                    Optional<UniqueStructureElement> op = allUniques.stream().filter(f -> longcompare(f.getId(), curr.get().getIdUniqueStructure()))
                            .findFirst();
                    if (op.isPresent()){
                        UniqueStructureElement.NpaTextName key = op.get().getGroupping();
                        List<UniqueStructureElement> uns = grUniqueLinks.get(key);
                        for (UniqueStructureElement un:
                             uns) {
                            List<UniqueRealStructureElement> arr = grRealLinks.get(un.getId());
                            if (arr != null){
                                rsels.addAll(arr);
                            }
                        }
                    }

                    List<Long> revMapped = rsels.stream().map(s -> s.getIdRev()).collect(Collectors.toList());
                    //Ссылка актуальна
                    List<Long> revOk = mm.getValue().stream()
                            .map(f -> f.getIdRevision())
                            .collect(Collectors.toList());
                    //Ссылка неактуальна
                    List<Long> revNot = new ArrayList<>();
                    for (ListNotActual lnact:
                            lnacts) {
                        for (UniqueRealStructureElement urst :
                                rsels) {
                            if (longcompare(urst.getIdRev(), lnact.getListNotActualPK().getIdRev())
                                    && StringUtils.equals(urst.getIdRealStructure(), lnact.getListNotActualPK().getIdSource()))
                                revNot.add(urst.getIdRev());
                        }
                    }
                    //Положения не найдены
                    List<Long> revNotFound = rsels.stream()
                            .filter(f -> f.getIdRealStructure() == null)
                            .map(f -> f.getIdRev())
                            .collect(Collectors.toList());

                    //Если сумма не сходится - значит, есть неопределенности
                    st = (revOk.size() + revNot.size() + revNotFound.size()) < npaRevsCache.get(vnpa.getIdSource()).size();
                }
                else
                    st = true;
                vld.setStatus(st);
                current.getLinks().add(vld);
            }
        }

        //Отбираем случаи с неопределенностью
        return violationTreeCache.values()
                .stream().filter(v -> v.getLinks().stream().anyMatch(l -> l.getStatus()))
                .sorted(Comparator.comparing(ViolationTreeData::getText))
                .collect(Collectors.toList());
    }

    private ViolationLinkData createLink(Map.Entry<String, List<NVA_SPR_FKAU_ViolationNPAStructure>> ll, List<NVA_SPR_AUD_NPA_Revision> revs, NVA_SPR_FKAU_ViolationNPA lnpa, List<NVA_SPR_AUD_NPA> dels){
        Optional<NVA_SPR_FKAU_ViolationNPAStructure> st =  ll.getValue().stream()
                .filter(s -> StringUtils.equals(s.getIdSource(), s.getIdRevisionStructure()))
                .findFirst();
        NVA_SPR_AUD_NPA_Revision r = null;
        if (st.isPresent()){
            r = revs.stream().filter(rr -> longcompare(rr.getId(), st.get().getIdRevision())).findFirst().orElse(null);
        }
        boolean isDel = dels.stream().anyMatch(d -> longcompare(d.getId(), lnpa.getIdSource()));
        return new ViolationLinkData(ll.getValue().get(0).getText(), lnpa, ll.getValue().get(0), r, false, isDel);
    }

    private ViolationLinkData createLink(NVA_SPR_FKAU_ViolationNPA lnpa, List<NVA_SPR_AUD_NPA> dels){
        boolean isDel = dels.stream().anyMatch(d -> longcompare(d.getId(), lnpa.getIdSource()));
        return new ViolationLinkData("Весь документ", lnpa, null, null, false, isDel);
    }
}
