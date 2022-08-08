package ru.nvacenter.bis.auditviolationnpa.services.dao;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.StructureHierarchy;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by oshesternikova on 01.03.2018.
 * Анализ дат начала и конца действия
 */
@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class DateStructureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FZDocumentService.class);
    private static final String ERROR_MESSAGE = "Ошибка при выполнении запроса '%s'";

    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;

    /**
     * Найти даты начала-конца действия положения (рекурсивно)
     * @param docId
     * @param revId
     * @param strId
     * @return
     */
    public Pair<LocalDate, LocalDate> findDateStartAndEnd(Long docId, Optional<Long> revId, String strId){
        LocalDate dtStRev = null;
        LocalDate dtEndRev = null;

        String qstr = "select new ru.nvacenter.bis.auditviolationnpa.domain.dto.StructureHierarchy(r.id, r.fk_parentElementId, r.dateBegin, r.dateEnd) from NVA_SPR_AUD_NPA_STRUCTURE r " +
                "where r.id_SprAudNPA = :npa and r.deleted is null";
        if (revId.isPresent())
            qstr += " and r.id_SprAudNPAStr = :rev";
        else
            qstr += " and r.id_SprAudNPAStr is null";
        TypedQuery<StructureHierarchy> q = entityManager.createQuery(qstr, StructureHierarchy.class);
        q.setParameter("npa", docId);
        if (revId.isPresent())
            q.setParameter("rev", revId.get());
        List<StructureHierarchy> strs = q.getResultList();
        StructureHierarchy stForDates = tryFindRecursive(strId, strs);
        if (stForDates != null){
            dtStRev = stForDates.getDateStart();
            dtEndRev = stForDates.getDateEnd();
        }


        if (revId.isPresent() && dtStRev == null && dtEndRev == null){
            Query qr = entityManager.createQuery("select r.dtBegin, r.dtEnd from NVA_SPR_AUD_NPA_Revision r where r.id = :rev");
            qr.setParameter("rev", revId.get());
            Object[] arr = (Object[])qr.getSingleResult();
            dtStRev = (LocalDate)arr[0];
            dtEndRev = (LocalDate)arr[1];
        }
        if (dtStRev == null) dtStRev = LocalDate.of(2010, 1,1);
        Pair<LocalDate, LocalDate> st_end = new ImmutablePair<>(dtStRev, dtEndRev);
        return  st_end;
    }

    private StructureHierarchy tryFindRecursive(String id, List<StructureHierarchy> rs){
        StructureHierarchy sh = rs.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
        if (sh == null) return null;
        if (sh.getDateEnd() != null || sh.getDateStart() != null) return  sh;
        if (sh.getParent_id() == null) return null;
        return tryFindRecursive(sh.getParent_id(), rs);
    }

}
