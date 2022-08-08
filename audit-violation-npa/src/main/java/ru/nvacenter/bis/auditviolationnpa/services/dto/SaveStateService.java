package ru.nvacenter.bis.auditviolationnpa.services.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPA;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPAStructure;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.ListNotActual;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.ListNotActualPK;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.UniqueRealStructureElement;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.UniqueStructureElement;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.ViolationRevData;
import ru.nvacenter.bis.auditviolationnpa.models.SaveState;
import ru.nvacenter.bis.auditviolationnpa.services.dao.DateStructureService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.ListNotActualService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.UniqueStructureService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.ViolationsService;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dmihaylov on 26.04.2018.
 */

@Service
public class SaveStateService {
    @Inject
    ViolationsService violationsService;
    @Inject
    ListNotActualService listNotActualService;
    @Inject
    UniqueStructureService uniqueStructureService;
    @Inject
    DateStructureService dateStructureService;
    @Inject
    UniqueCreateStructureService uniqueCreateStructureService;
    /***
     * Попробовать поменять статус
     * @return
     */
    public boolean saveState(SaveState saveState) {
        NVA_SPR_FKAU_ViolationNPAStructure str = violationsService.findLink(saveState.getIdNpaLink());
        if(str != null){
            NVA_SPR_FKAU_ViolationNPA lnpa = violationsService.findLinkNpa(str.getIdNPA());
            Long docId = lnpa.getIdSource();
            String strId = str.getIdRevisionStructure() == null ? str.getIdSource() : str.getIdRevisionStructure();
            Optional<Long> revId = Optional.of(str.getIdRevision());
            Map<UniqueStructureElement, List<UniqueRealStructureElement>> maps =  uniqueStructureService.findEquivalentClasses(strId, Optional.of(str.getIdRevision()), docId);
            if (maps.size() == 0){
                uniqueCreateStructureService.createOrUpdateUniqueElements(strId, revId.get(), docId);
                //Достаем классы эквивалетности
                maps = uniqueStructureService.findEquivalentClasses(strId, revId, docId);
            }
            UniqueStructureElement key = null;
            for (Map.Entry<UniqueStructureElement, List<UniqueRealStructureElement>> mm:
                    maps.entrySet()) {
                boolean b = mm.getValue()
                        .stream()
                        .anyMatch(m -> /*StringUtils.equals(strId, m.getIdRealStructure()) &&*/ saveState.getIdRevs().contains(m.getIdRev()));
                if (b){
                    key = mm.getKey();
                    break;
                }
            }

            if (key == null) return false;

            List<NVA_SPR_FKAU_ViolationNPAStructure> newStrs = new ArrayList<>();
            List<NVA_SPR_FKAU_ViolationNPAStructure> oldStrs = new ArrayList<>();

            List<ListNotActual> newfailed = new ArrayList<>();
            List<ListNotActual> oldfailed = new ArrayList<>();
            Set<NVA_SPR_FKAU_ViolationNPAStructure> strs = lnpa.getStructures(); /*violationsService.findAllLinksStr(lnpa.getId());*/
            //Ссылки, которые существуют
            List<NVA_SPR_FKAU_ViolationNPAStructure> coll = strs.stream()
                    .filter(ss -> StringUtils.equals(ss.getIdSource(), str.getIdSource()) && saveState.getIdRevs().contains(ss.getIdRevision()))
                    .collect(Collectors.toList());
            //Ссылки, в которых отказано
            List<ListNotActual> notFound = listNotActualService.find(lnpa.getId())
                    .stream()
                    .filter(ln -> saveState.getIdRevs().contains(ln.getListNotActualPK().getIdRev()))
                    .collect(Collectors.toList());

            switch (saveState.getOldState())
            {
                case YES:
                    oldStrs.addAll(coll);
                    break;
                case NO:
                    oldfailed.addAll(notFound);
                    break;
            }

            switch (saveState.getNewState())
            {
                case YES:
                    List<NVA_SPR_FKAU_ViolationNPAStructure> newVals = createYes(str, maps.get(key), docId);
                    newStrs.addAll(newVals);
                    break;
                case NO:
                    List<ListNotActual> newValsN = createNo(lnpa.getId(), maps.get(key));
                    newfailed.addAll(newValsN);
                    break;
            }

            if (newStrs.size() > 0)
                violationsService.save(newStrs);

            if (oldStrs.size() > 0) {
                for (NVA_SPR_FKAU_ViolationNPAStructure c : coll) {
                    lnpa.getStructures().remove(c);
                }
                violationsService.remove(oldStrs);
            }
            if (newfailed.size() > 0)
                listNotActualService.save(newfailed);

            if (oldfailed.size() > 0)
                listNotActualService.remove(oldfailed);
        }
        return true;
    }

    private List<NVA_SPR_FKAU_ViolationNPAStructure> createYes(NVA_SPR_FKAU_ViolationNPAStructure proto, List<UniqueRealStructureElement> reals, Long docId){
        List<NVA_SPR_FKAU_ViolationNPAStructure> res = new ArrayList<>(reals.size());
        for (UniqueRealStructureElement r :
                reals) {
            NVA_SPR_FKAU_ViolationNPAStructure str = new NVA_SPR_FKAU_ViolationNPAStructure();
            str.setIdSource(proto.getIdSource());
            str.setIdNPA(proto.getIdNPA());
            str.setText(proto.getText());
            str.setIdRevision(r.getIdRev());
            str.setIdRevisionStructure(r.getIdRealStructure());
            Pair<LocalDate, LocalDate> dts = dateStructureService.findDateStartAndEnd(docId, Optional.of(r.getIdRev()), r.getIdRealStructure());
            str.setDateBegin(dts.getLeft());
            str.setDateEnd(dts.getRight());
            res.add(str);
        }
        return res;
    }

    private List<ListNotActual> createNo(Long idViolNpa, List<UniqueRealStructureElement> reals){
        List<ListNotActual> res = new ArrayList<>(reals.size());
        for (UniqueRealStructureElement r:
             reals) {
            ListNotActual lna = new ListNotActual();
            ListNotActualPK pk = new ListNotActualPK(idViolNpa, r.getIdRev(), r.getIdRealStructure());
            lna.setListNotActualPK(pk);
            res.add(lna);
        }
        return res;
    }

    private static boolean longcompare(Long l1, Long l2){
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        return Long.compare(l1, l2) == 0;
    }

}
