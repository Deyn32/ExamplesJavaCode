package ru.nvacenter.bis.audit.npa.services.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_ElementType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by alaptev on 03.02.2017.
 */
@Service
public class NVA_SPR_AUD_ElementTypeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NVA_SPR_AUD_ElementTypeService.class);

    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;

    public List<NVA_SPR_AUD_ElementType> getElements(LocalDate date) {
        TypedQuery<NVA_SPR_AUD_ElementType> query = entityManager.createNamedQuery("audit-npa-common-mappings.getElements", NVA_SPR_AUD_ElementType.class);
        query.setParameter("date", date);

        return query.getResultList();
    }
}