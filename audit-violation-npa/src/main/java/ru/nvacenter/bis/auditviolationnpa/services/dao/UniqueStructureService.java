package ru.nvacenter.bis.auditviolationnpa.services.dao;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.UniqueRealStructureElement;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.UniqueStructureElement;
import ru.nvacenter.bis.auditviolationnpa.models.*;
import ru.nvacenter.bis.core.server.domain.User;

import javax.persistence.*;
import java.util.*;

/**
 * Created by oshesternikova on 21.02.2018.
 */
@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class UniqueStructureService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FZDocumentService.class);
    private static final String ERROR_MESSAGE = "Ошибка при выполнении запроса '%s'";

    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;

    /**
     * Найти уникальный элемент структуры
     * @param anyStructureId ID любого структурного элемента из редакций
     * @param revId ID редакции
     * @param docId ID НПА
     * @return
     */
    public UniqueStructureElement find(String anyStructureId, Optional<Long> revId, Long docId){
        String baseQuery = "select u from UniqueStructureElement as u inner join u.reals as r "+
                "where u.npa = :npa and r.idRealStructure = :stri ";
        if (revId.isPresent())
            baseQuery += " and r.idRev = :rev";
        else
            baseQuery += " and r.idRev is null";
        TypedQuery<UniqueStructureElement> tq =  entityManager.createQuery(baseQuery, UniqueStructureElement.class);
        tq.setParameter("npa", docId);
        tq.setParameter("stri", anyStructureId);
        if (revId.isPresent()) tq.setParameter("rev", revId.get());
        List<UniqueStructureElement> l = tq.getResultList();
        if (l.size() == 0) return null;
        return l.get(0);
    }

    public List<UniqueStructureElement> find(List<Long> ids){
        if (ids.size() == 0) return new ArrayList<>();
        TypedQuery<UniqueStructureElement> q = entityManager.createQuery("from UniqueStructureElement e where e.id in :ids", UniqueStructureElement.class);
        q.setParameter("ids", ids);
        List<UniqueStructureElement> l = q.getResultList();
        return l;
    }

    public List<UniqueStructureElement> findByNpa(Long npa){
        TypedQuery<UniqueStructureElement> q = entityManager.createQuery("from UniqueStructureElement e where e.npa = :npa", UniqueStructureElement.class);
        q.setParameter("npa", npa);
        List<UniqueStructureElement> l = q.getResultList();
        return l;
    }

    /**
     * Создать ссылку "под подозрением"
     * @param revs Редакции
     * @param el Уникальный элемент
     * @return
     */
    public List<UniqueRealStructureElement> createEmptyLinks(List<Long> revs, UniqueStructureElement el){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UniqueRealStructureElement> arr = new ArrayList<>();
        for (Long rev : revs){
            UniqueRealStructureElement r = new UniqueRealStructureElement();
            r.setIdRev(rev);
            r.setIdRealStructure(null);
            r.setIdUniqueStructure(el.getId());
            r.setUserModify(user.getFullname());
            r.setDateModify(new Date());
            arr.add(r);
        }
        return arr;
    }

    /**
     * Создать уникальный элемент структуры
     * @param npa ID НПА
     * @param path Путь к узлу в дереве
     * @return
     */
    public UniqueStructureElement create(Long npa, Path path){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UniqueStructureElement el = new UniqueStructureElement();
        el.setNpa(npa);
        el.setName(path.toString());
        el.setDateModify(new Date());
        el.setUserModify(user.getFullname());
        entityManager.persist(el);
        entityManager.flush();
        return el;
    }

    /**
     * Создать привязки реальных элементов структры из редакций к уникальной
     * @param strs Элементы структры из редакций
     * @param el Уникальная единица
     */
    public List<UniqueRealStructureElement> createLinks(List<NVA_SPR_AUD_NPA_STRUCTURE> strs, UniqueStructureElement el){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UniqueRealStructureElement> ls = new ArrayList<>();
        for (NVA_SPR_AUD_NPA_STRUCTURE ss:
             strs) {
            UniqueRealStructureElement r = new UniqueRealStructureElement();
            r.setIdRev(ss.getId_SprAudNPAStr());
            r.setIdRealStructure(ss.getId());
            r.setIdUniqueStructure(el.getId());
            r.setUserModify(user.getFullname());
            r.setDateModify(new Date());
            ls.add(r);
        }
        return ls;
    }

    /**
     * Сохранить ссылки
     * @param ls Список ссылок
     */
    public void saveLinks(List<UniqueRealStructureElement> ls){
        for(UniqueRealStructureElement u : ls){
            if (u.getId() == null)
                entityManager.persist(u);
            else
                entityManager.merge(u);
        }
        entityManager.flush();
    }

    /**
     * Удалить
     * @param rs
     */
    public void remove(List<UniqueRealStructureElement> rs){
        for (UniqueRealStructureElement r :
                rs) {
            entityManager.remove(entityManager.contains(r) ? r : entityManager.merge(r));
        }

        entityManager.flush();
    }
    /**
     * Найти привязки реальных элементов структры из редакций к уникальной
     * @param el Уникальная единица
     * @return
     */
    public List<UniqueRealStructureElement> findLinks(UniqueStructureElement el){
        TypedQuery<UniqueRealStructureElement> tq =
                entityManager.createQuery("from UniqueRealStructureElement as r where r.idUniqueStructure = :un and r.isDeleted = 0", UniqueRealStructureElement.class);
        tq.setParameter("un", el.getId());
        List<UniqueRealStructureElement> l = tq.getResultList();
        return l;

    }

    /**
     * Попытаться установить редакцию, исходя из номера НПА и элемента
     * @param npa НПА
     * @param idStr ID положения
     * @return
     */
    public Long tryFindRevision(Long npa, String idStr){
        Query q = entityManager.createQuery("select r.id_SprAudNPAStr from NVA_SPR_AUD_NPA_STRUCTURE r where r.id = :id and r.id_SprAudNPA = :npa and r.deleted is null");
        q.setParameter("id", idStr);
        q.setParameter("npa", npa);
        List<Object> obs = q.getResultList();
        if (obs.size() == 1) {
            Object o = obs.get(0);
            if (o == null) return null;
            return (Long)o;
        }
        return null;
    }

    /***
     * Неустановленные случаи соответствия в редакциях
     * @return
     */
    public List<UniqueRealStructureElement> findEmptyLinks(){
        TypedQuery<UniqueRealStructureElement> tq =
                entityManager.createQuery("from UniqueRealStructureElement as r where r.idRealStructure is null ", UniqueRealStructureElement.class);

        List<UniqueRealStructureElement> l = tq.getResultList();
        return l;
    }

    /**
     * Список всех положений
     * @return
     */
    public List<UniqueRealStructureElement> findAllLinks(){
        TypedQuery<UniqueRealStructureElement> tq =
                entityManager.createQuery("from UniqueRealStructureElement as r where r.isDeleted = 0", UniqueRealStructureElement.class);

        List<UniqueRealStructureElement> l = tq.getResultList();
        return l;
    }

    /**
     * Список всех уникальных положений
     * @return
     */
    public List<UniqueStructureElement> findAllUniques(){
        TypedQuery<UniqueStructureElement> tq =
                entityManager.createQuery("from UniqueStructureElement as r", UniqueStructureElement.class);

        List<UniqueStructureElement> l = tq.getResultList();
        return l;
    }


    /**
     * Элемент и все значения в конкретной редакции
     * @param anyStructureId ID положения из любой редакции
     * @param revId ID редакции
     * @param docId ID документа
     * @return
     */
    public Map<UniqueStructureElement, List<UniqueRealStructureElement>> findEquivalentClasses(String anyStructureId, Optional<Long> revId, Long docId){
        UniqueStructureElement el = find(anyStructureId, revId, docId);
        Map<UniqueStructureElement, List<UniqueRealStructureElement>> mps = new HashMap<>();
        if (el != null){
            TypedQuery<UniqueStructureElement> usels = entityManager.createQuery("from UniqueStructureElement u where u.npa = :npa and u.name = :name", UniqueStructureElement.class);
            usels.setParameter("name", el.getName());
            usels.setParameter("npa", el.getNpa());
            List<UniqueStructureElement> lst = usels.getResultList();

            for (UniqueStructureElement uel :
                    lst) {
                List<UniqueRealStructureElement> lst1 = findLinks(uel);
                mps.put(uel, lst1);
            }
        }

        return mps;

    }
}
