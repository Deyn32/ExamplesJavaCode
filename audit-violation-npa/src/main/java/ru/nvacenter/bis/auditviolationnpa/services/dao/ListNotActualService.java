package ru.nvacenter.bis.auditviolationnpa.services.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.auditviolationnpa.domain.dao.ListNotActual;
import ru.nvacenter.bis.core.server.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by oshesternikova on 25.04.2018.
 */
@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class ListNotActualService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FZDocumentService.class);
    private static final String ERROR_MESSAGE = "Ошибка при выполнении запроса '%s'";

    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;

    /***
     * Найти неактуальные редакции для нарушения и источника
     * @param viol Нарушение
     * @return
     */
    public List<ListNotActual> find(Long viol){
        TypedQuery<ListNotActual> tq = entityManager.createQuery("from ListNotActual l where l.listNotActualPK.idViolationNpa = :viol", ListNotActual.class);
        tq.setParameter("viol", viol);
        List<ListNotActual> lst = tq.getResultList();
        return lst;
    }

    public void save(List<ListNotActual> lst){
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        for (ListNotActual l :
                lst) {
            l.setUserModify(user.getFullname());
            l.setDateModify(LocalDate.now());
            entityManager.merge(l);
        }
        entityManager.flush();
    }

    public void remove(List<ListNotActual> lst){
        for (ListNotActual l :
                lst) {
            entityManager.remove(entityManager.contains(l) ? l : entityManager.merge(l));
        }
        entityManager.flush();
    }
}
