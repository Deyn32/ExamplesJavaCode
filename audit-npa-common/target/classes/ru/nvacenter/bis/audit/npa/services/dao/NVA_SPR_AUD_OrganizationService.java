package ru.nvacenter.bis.audit.npa.services.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_Organization;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by oshesternikova on 07.03.2018.
 */
@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class NVA_SPR_AUD_OrganizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NVA_SPR_AUD_RevisionService.class);

    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;
    List<NVA_SPR_AUD_Organization> getAll(){
        TypedQuery<NVA_SPR_AUD_Organization> l = entityManager.createQuery("from NVA_SPR_AUD_Organization o where o.isDeleted = 0", NVA_SPR_AUD_Organization.class);
        return l.getResultList();
    }
}
