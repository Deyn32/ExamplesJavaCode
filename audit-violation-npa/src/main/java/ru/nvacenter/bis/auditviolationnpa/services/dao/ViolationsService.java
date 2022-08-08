package ru.nvacenter.bis.auditviolationnpa.services.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPA;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPAStructure;
import ru.nvacenter.bis.auditviolationcommon.server.domain.ViolationData;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.Violation;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.ViolationTreeData;
import ru.nvacenter.bis.core.server.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by oshesternikova on 28.02.2018.
 */
@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="jpaTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class ViolationsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FZDocumentService.class);
    private static final String ERROR_MESSAGE = "Ошибка при выполнении запроса '%s'";

    @PersistenceContext(unitName = "appPersistenceUnit")
    private EntityManager entityManager;

    /***
     * Все актуальные ссылки нарушений
     * @return
     */
    public List<NVA_SPR_FKAU_ViolationNPA> findAllLinks(){
        TypedQuery<NVA_SPR_FKAU_ViolationNPA> tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPA n where n.isDeleted = 0", NVA_SPR_FKAU_ViolationNPA.class);
        List<NVA_SPR_FKAU_ViolationNPA> l = tq.getResultList();
        return l;
    }

    /***
     * Все актуальные ссылки нарушений для НПА
     * @return
     */
    public List<NVA_SPR_FKAU_ViolationNPA> findAllLinks(Long idNpa){
        TypedQuery<NVA_SPR_FKAU_ViolationNPA> tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPA n where n.idSource = :npa and n.isDeleted = 0", NVA_SPR_FKAU_ViolationNPA.class);
        tq.setParameter("npa", idNpa);
        List<NVA_SPR_FKAU_ViolationNPA> l = tq.getResultList();
        return l;
    }

    /***
     * Все актуальные ссылки нарушений для нарушения
     * @return
     */
    public List<NVA_SPR_FKAU_ViolationNPA> findAllLinksViol(Long idViol){
        TypedQuery<NVA_SPR_FKAU_ViolationNPA> tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPA n where n.idViolation = :v and n.isDeleted = 0", NVA_SPR_FKAU_ViolationNPA.class);
        tq.setParameter("v", idViol);
        List<NVA_SPR_FKAU_ViolationNPA> l = tq.getResultList();
        return l;
    }

    /***
     * Все актуальные ссылки нарушений для нарушения
     * @return
     */
    public List<NVA_SPR_FKAU_ViolationNPA> findAllLinksViolNpa(Long idNpa, Long idViol){
        TypedQuery<NVA_SPR_FKAU_ViolationNPA> tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPA n where n.idViolation = :v and n.idSource = :npa and n.isDeleted = 0", NVA_SPR_FKAU_ViolationNPA.class);
        tq.setParameter("v", idViol);
        tq.setParameter("npa", idNpa);
        List<NVA_SPR_FKAU_ViolationNPA> l = tq.getResultList();
        return l;
    }

    /**
     * Актуальные ссылки нарушений
     * @return
     */
    public List<NVA_SPR_FKAU_ViolationNPAStructure> findAllLinksStr(){

        TypedQuery<NVA_SPR_FKAU_ViolationNPAStructure> tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPAStructure n where n.isDeleted = 0", NVA_SPR_FKAU_ViolationNPAStructure.class);
        List<NVA_SPR_FKAU_ViolationNPAStructure> l = tq.getResultList();
        return l;
    }

    /**
     * Актуальные ссылки нарушений для документа
     * @return
     */
    public List<NVA_SPR_FKAU_ViolationNPAStructure> findAllLinksStr(Long idLinkNpa){

        TypedQuery<NVA_SPR_FKAU_ViolationNPAStructure> tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPAStructure n where n.idNPA = :npa and n.isDeleted = 0", NVA_SPR_FKAU_ViolationNPAStructure.class);
        tq.setParameter("npa", idLinkNpa);
        List<NVA_SPR_FKAU_ViolationNPAStructure> l = tq.getResultList();
        return l;
    }

    /**
     * Актуальные ссылки нарушений
     * @return
     */
    public List<NVA_SPR_FKAU_ViolationNPAStructure> findAllLinksStr(NVA_SPR_FKAU_ViolationNPAStructure proto){

        TypedQuery<NVA_SPR_FKAU_ViolationNPAStructure> tq = null;
        if (proto.getIdSource() != null){
            tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPAStructure n where n.idNPA = :npa and n.idSource = :source and n.isDeleted = 0", NVA_SPR_FKAU_ViolationNPAStructure.class);
            tq.setParameter("source", proto.getIdSource());
        }
        else{
            tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPAStructure n where n.idNPA = :npa and n.idSource is null and n.isDeleted = 0", NVA_SPR_FKAU_ViolationNPAStructure.class);
        }
        tq.setParameter("npa", proto.getIdNPA());
        List<NVA_SPR_FKAU_ViolationNPAStructure> l = tq.getResultList();
        return l;
    }


    /***
     * Сохранить новые
     * @param strs
     */
    public void save(List<NVA_SPR_FKAU_ViolationNPAStructure> strs){
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        for (NVA_SPR_FKAU_ViolationNPAStructure s:
             strs) {
            s.setDateModify(LocalDate.now());
            s.setUserModify(user.getFullname());
            if (s.getId() == null)
                entityManager.persist(s);
            else
                entityManager.merge(s);

        }
        entityManager.flush();
    }

    public void remove(List<NVA_SPR_FKAU_ViolationNPAStructure> strs){
        for (NVA_SPR_FKAU_ViolationNPAStructure s:
                strs) {
            entityManager.remove(entityManager.contains(s) ? s : entityManager.merge(s));

        }
        entityManager.flush();
    }

    public ViolationData find(Long id){
        return entityManager.find(ViolationData.class, id);
    }

    public List<ViolationData> findAllViol() {
        TypedQuery<ViolationData> tq = entityManager.createQuery("from ViolationData n where n.isDeleted = 0", ViolationData.class);
        List<ViolationData> l = tq.getResultList();
        return l;
    }


    /**Ссылку по id
     *
     * @param id
     * @return
     */
    public NVA_SPR_FKAU_ViolationNPA findLinkNpa(Long id){
        return entityManager.find(NVA_SPR_FKAU_ViolationNPA.class, id);
    }

    /**Ссылку по id
     *
     * @param id
     * @return
     */
    public NVA_SPR_FKAU_ViolationNPAStructure findLink(Long id){
        return entityManager.find(NVA_SPR_FKAU_ViolationNPAStructure.class, id);
    }

    /**dmihaylov
     *
     *Все ссылки без НПА
     * @return
     */
    public List<NVA_SPR_FKAU_ViolationNPAStructure> findAllUnloadNPA(){
        TypedQuery<NVA_SPR_FKAU_ViolationNPAStructure> tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPAStructure n where n.idSource = null and n.isDeleted = 0", NVA_SPR_FKAU_ViolationNPAStructure.class);
        List<NVA_SPR_FKAU_ViolationNPAStructure> l = tq.getResultList();
        return l;
    }

    /**dmihaylov
     * Все ссылки без редакций
     * @return
     */
    public List<NVA_SPR_FKAU_ViolationNPAStructure> findAllWithoutRevNPA() {
        TypedQuery<NVA_SPR_FKAU_ViolationNPAStructure> tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPAStructure n where n.idRevision is null and n.isDeleted = 0 and n.idSource is not null", NVA_SPR_FKAU_ViolationNPAStructure.class);
        List<NVA_SPR_FKAU_ViolationNPAStructure> l = tq.getResultList();
        return l;
    }

    /**dmihaylov
     * Находим все линки по id нарушений
     * @param idViolation id нарушения
     * @return
     */
    public List<NVA_SPR_FKAU_ViolationNPA> findLinksNpaOnIdViol(Long idViolation) {
        TypedQuery<NVA_SPR_FKAU_ViolationNPA> tq = entityManager.createQuery("from NVA_SPR_FKAU_ViolationNPA n where n.idViolation = :id and n.isDeleted = 0", NVA_SPR_FKAU_ViolationNPA.class);
        tq.setParameter("id", idViolation);
        List<NVA_SPR_FKAU_ViolationNPA> links = tq.getResultList();
        return links;
    }

    /**dmihaylov
     *
     */
    public void findViolation(ViolationTreeData vtd) {
        Query q = entityManager.createQuery("update ViolationData n set n.dateBegin = :dtb, n.dateEnd = :dte where n.id = :id");
        q.setParameter("dtb", vtd.getDtBegin());
        q.setParameter("dte", vtd.getDtEnd());
        q.setParameter("id", vtd.getId());
        q.executeUpdate();
    }

    public String findIdNpaStruct(Long idNpaStruct){
        List<String> s = new ArrayList<>();
        TypedQuery<String> q = entityManager.createQuery("select n.idSource from NVA_SPR_FKAU_ViolationNPAStructure n where n.id = :idNpaStruct", String.class);
        q.setParameter("idNpaStruct", idNpaStruct);
        s.addAll(q.getResultList());
        return s.get(0);
    }
}
