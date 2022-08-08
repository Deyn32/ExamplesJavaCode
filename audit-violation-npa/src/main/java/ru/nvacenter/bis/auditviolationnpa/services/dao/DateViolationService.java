/**
 * Created by dmihaylov on 17.04.2018.
 */
package ru.nvacenter.bis.auditviolationnpa.services.dao;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPA;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPAStructure;
import ru.nvacenter.bis.auditviolationcommon.server.domain.ViolationData;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.Violation;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.ViolationTreeData;
import ru.nvacenter.bis.auditviolationnpa.services.dto.ViolationsTreeService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class DateViolationService {
    @PersistenceContext(unitName = "appPersistenceUnit")
    private EntityManager entityManager;
    @Inject
    ViolationsService violationsService;
    @Inject
    ViolationsTreeService violationsTreeService;

    //Соединяем
    public void updateDateViolation(List<Long> listIdViolations) {
        List<ViolationData> listViols = findViol(listIdViolations);
        List<Violation> listViolations = initViolation(listViols);
        for(Violation viol : listViolations) {
            for(NVA_SPR_FKAU_ViolationNPA linkNpa : viol.getListLinksNpa()){
                List<NVA_SPR_FKAU_ViolationNPAStructure> listLinks = findListLinks(linkNpa.getId());
                viol.setDtBegin(dateBeginMin(listLinks));
                viol.setDtEnd(dateEndMax(listLinks));
            }
        }
        saveViols(listViols, listViolations);
    }

    //Поднимаем нарушения
    private List<ViolationData> findViol(List<Long> listIdViolations) {
        List<ViolationData> listViols = new ArrayList<>();
        for (Long id : listIdViolations) {
            listViols.add(violationsService.find(id));
        }
        return listViols;
    }

    private List<Violation> initViolation(List<ViolationData> listViols) {
        List<Violation> listViolations = new ArrayList<>();
        for(ViolationData viol : listViols) {
            Violation violation = new Violation();
            List<NVA_SPR_FKAU_ViolationNPA> listLinksNpa = findListLinksNpa(viol);
            violation.setId(viol.getId());
            violation.setListLinksNpa(listLinksNpa);
            listViolations.add(violation);
        }
        return listViolations;
    }



    //Поднимаем записи ссылок нпа по id
    private List<NVA_SPR_FKAU_ViolationNPA> findListLinksNpa(ViolationData viol) {
        return violationsService.findLinksNpaOnIdViol(viol.getId());
    }

    //Поднимаем все записи по idNpa
    public List<NVA_SPR_FKAU_ViolationNPAStructure> findListLinks(Long idNpa) {
        return violationsService.findAllLinksStr(idNpa);
    }

    public boolean saveEditDateViol(ViolationTreeData vtd){
        try{
            violationsService.findViolation(vtd);
        }catch (Exception ex){
            return false;
        }

        return true;
    }


    //Из этих записей мы берем дату начала (Самое маленькое)
    private LocalDate dateBeginMin(List<NVA_SPR_FKAU_ViolationNPAStructure> listLinks) {
        LocalDate minDate = null;
        for(NVA_SPR_FKAU_ViolationNPAStructure link : listLinks) {
            if(minDate == null)
                minDate = link.getDateBegin();
            else {
                if(minDate.compareTo(link.getDateBegin()) < 0)
                    minDate = link.getDateBegin();
            }
        }
        return minDate;
    }

    //И берем дату окончания (Самую большую или null)
    private LocalDate dateEndMax(List<NVA_SPR_FKAU_ViolationNPAStructure> listLinks) {
        LocalDate maxDate = null;
        for (NVA_SPR_FKAU_ViolationNPAStructure link : listLinks) {
            if (maxDate == null) {
                if(link.getDateEnd() == null)
                    return null;
                else {
                    maxDate = link.getDateEnd();
                }
            } else {
                if (link.getDateEnd() == null) return null;
                else {
                    if(maxDate.compareTo(link.getDateEnd()) > 0)
                        maxDate = link.getDateEnd();
                }
            }
        }
        return maxDate;
    }

    public Collection<ViolationTreeData> greaterOrEquailCurrentDate(LocalDate date) {
        Collection<ViolationTreeData> vdatas = violationsTreeService.findViolationsAsTree();
        Collection<ViolationTreeData> newVdates = new ArrayList<>(vdatas.size());
        for (ViolationTreeData vtd: vdatas) {
            boolean isInInterval = (vtd.getDtBegin().isBefore(date) || vtd.getDtBegin().isEqual(date))
                   &&  (vtd.getDtEnd() == null || vtd.getDtEnd().isEqual(date) || vtd.getDtEnd().isAfter(date));
           if (isInInterval)
               newVdates.add(vtd);
        }
        return newVdates;
    }

    private void saveViols(List<ViolationData> listViols, List<Violation> listViolations) {
        for(ViolationData viol : listViols){
            for(Violation violation : listViolations) {
                if(viol.getId() == violation.getId()) {
                    viol.setDateBegin(violation.getDtBegin());
                    viol.setDateEnd(violation.getDtEnd());
                    break;
                }
            }
            entityManager.persist(viol);
        }
        entityManager.flush();
    }
}
