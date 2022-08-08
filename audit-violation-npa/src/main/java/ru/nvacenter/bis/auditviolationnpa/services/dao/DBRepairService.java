package ru.nvacenter.bis.auditviolationnpa.services.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_NPAService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_RevisionService;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPAStructure;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.UniqueRealStructureElement;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.UniqueStructureElement;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by oshesternikova on 11.04.2018.
 */
@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class DBRepairService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FZDocumentService.class);
    private static final String ERROR_MESSAGE = "Ошибка при выполнении запроса '%s'";

    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;

    @Inject
    FZDocumentService fzDocumentService;

    @Inject
    NVA_SPR_AUD_RevisionService nva_SPR_AUD_RevisionService;

    @Inject
    NVA_SPR_AUD_NPAService nva_SPR_AUD_NPAService;

    @Inject
    UniqueStructureService uniqueStructureService;

    public void create(){
        TypedQuery<Long> q = entityManager.createQuery("select distinct n.id_SprAudNPA from NVA_SPR_AUD_NPA_STRUCTURE n where n.id_SprAudNPAStr is null and n.deleted is null", Long.class);
        List<Long> lst = q.getResultList();
        //Создаем редакции
        List<NVA_SPR_AUD_NPA> npas = nva_SPR_AUD_NPAService.find(lst);
        List<NVA_SPR_AUD_NPA_Revision> revs = new ArrayList<>(npas.size());
        for (NVA_SPR_AUD_NPA npa :
                npas) {
            NVA_SPR_AUD_NPA_Revision r = createNewRev(npa);
            revs.add(r);
        }
        nva_SPR_AUD_RevisionService.save(revs);
        //Структурные единицы
        for (NVA_SPR_AUD_NPA_Revision r :
                revs) {
            Query q1 = entityManager.createQuery("update NVA_SPR_AUD_NPA_STRUCTURE n set n.id_SprAudNPAStr = :rev where n.id_SprAudNPA = :npa and n.id_SprAudNPAStr is null and n.deleted is null");
            q1.setParameter("rev", r.getId());
            q1.setParameter("npa", r.getId_NPA());
            int l = q1.executeUpdate();
        }

        //Real Structure
        for (NVA_SPR_AUD_NPA_Revision r :
                revs) {
            List<UniqueStructureElement> els = uniqueStructureService.findByNpa(r.getId_NPA());
            for (UniqueStructureElement el :
                    els) {
                Query q1 = entityManager.createQuery("update UniqueRealStructureElement r set r.idRev = :rev where r.idUniqueStructure = :id and r.idRev is null");
                q1.setParameter("rev", r.getId());
                q1.setParameter("id", el.getId());
                int k = q1.executeUpdate();
            }
        }

        //Violations
    }

    private NVA_SPR_AUD_NPA_Revision createNewRev(NVA_SPR_AUD_NPA npa){
        NVA_SPR_AUD_NPA_Revision rev = new NVA_SPR_AUD_NPA_Revision();
        rev.setId_NPA(npa.getId());
        rev.setDtRev(npa.getDate());
        rev.setDtBegin(npa.getDate());
        return rev;
    }

    public List<Long> findIdsStructure(String idRealStruct, Long idNpa){
        List<Long> list = new ArrayList<>();
        TypedQuery<Long> q = entityManager.createQuery(
                        "select n.id from UniqueStructureElement n where n.npa = :idNpa " +
                        "and n.name = (" +
                        "select us.name from UniqueRealStructureElement urs " +
                        "join UniqueStructureElement us ON urs.idUniqueStructure = us.id " +
                        "where urs.idRealStructure = :idRealStruct)",
                Long.class);
        q.setParameter("idRealStruct", idRealStruct);
        q.setParameter("idNpa", idNpa);
        list.addAll(q.getResultList());
        return list;
    }

    public void deleteUniqueStrucrure(List<Long> idUS){
        Query q = entityManager.createQuery("delete from UniqueRealStructureElement n where n.idUniqueStructure in :idUS").
                setParameter("idUS", idUS);
        q.executeUpdate();

        Query q2 = entityManager.createQuery("delete from UniqueStructureElement n where n.id in :idUS").
                setParameter("idUS", idUS);
        q2.executeUpdate();
    }

}
