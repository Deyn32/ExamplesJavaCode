package ru.nvacenter.bis.audit.npa.services.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_Revision;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oshesternikova on 12.04.2017.
 * CRUD операции для NVA_SPR_AUD_NPA_Revision
 */

@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class NVA_SPR_AUD_RevisionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NVA_SPR_AUD_RevisionService.class);

    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;

    /* Добавить в базу НПА новую ревизию*/
    public void create(NVA_SPR_AUD_NPA_Revision rev){
        rev.setDateModify(LocalDate.now());
        rev.setDeleted(false);
        if (rev.getDtRev() == null) rev.setDtRev(LocalDate.now());
        entityManager.persist(rev);
        entityManager.flush();
    }

    /* Изменить */
    public void update(NVA_SPR_AUD_NPA_Revision rev, Long id){
        rev.setId(id);
        rev.setDeleted(false);
        rev.setDateModify(LocalDate.now());
        if (rev.getDtRev() == null) rev.setDtRev(LocalDate.now());
        entityManager.merge(rev);
        entityManager.flush();
    }

    /* Удалить */
    public void delete(Long id){
        Query q = entityManager.createQuery("update NVA_SPR_AUD_NPA_Revision n set n.isDeleted = 1 where n.id = :npa");
        q.setParameter("npa", id);
        q.executeUpdate();

        Query q2 = entityManager.createQuery("update NVA_SPR_AUD_NPA_STRUCTURE n set n.deleted = :dt where n.id_SprAudNPAStr = :npa");
        q2.setParameter("npa", id);
        q2.setParameter("dt", LocalDate.now());
        q2.executeUpdate();
    }

    public NVA_SPR_AUD_NPA_Revision find(Long id){
        if (id == null)
            return null;

        NVA_SPR_AUD_NPA_Revision item = entityManager.find(NVA_SPR_AUD_NPA_Revision.class, id);
        return item;
    }

    public List<NVA_SPR_AUD_NPA_Revision> find(List<Long> ids){
        if (ids.size() == 0) return new ArrayList<>();
        TypedQuery<NVA_SPR_AUD_NPA_Revision> q = entityManager.createQuery("from NVA_SPR_AUD_NPA_Revision r where r.id in :ids", NVA_SPR_AUD_NPA_Revision.class);
        q.setParameter("ids", ids);
        return q.getResultList();
    }
}
