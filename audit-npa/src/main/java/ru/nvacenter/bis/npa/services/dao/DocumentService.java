package ru.nvacenter.bis.npa.services.dao;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.npa.domain.dao.Document;
import ru.nvacenter.bis.npa.domain.dao.DocumentStructure;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by oshesternikova on 19.01.2017.
 * Сервис для работы с БД ДОКа
 */

@Service
@PreAuthorize("isAuthenticated()")
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class DocumentService {
    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;

    public Document find(String id) {
        if (id == null)
            return null;

        Document item = entityManager.find(Document.class, id);
        return item;
    }

    public List<Document> findList(String num, LocalDate dt){
        TypedQuery<Document> allDocsQ = entityManager.createQuery("from Document i where i.numberLaw like :qnum and i.delSign is null", Document.class);
        allDocsQ.setParameter("qnum", "%"+num+"%");
        List<Document> allDocs = allDocsQ.getResultList();
        int index = 0;
        while(true){
            if (index >= allDocs.size()) break;
            if (!dt.equals(allDocs.get(index).getDateLawAsDate()))
                allDocs.remove(index);
            else index++;
        }
        return  allDocs;
    }

    public List<Document> findList(NVA_SPR_AUD_NPA doc){
       return this.findList(doc.getNumber(), doc.getDate());
    }

    public List<DocumentStructure> findStructureList(Document doc){
        TypedQuery<DocumentStructure> allDocsQ = entityManager.createQuery("from DocumentStructure i where i.fkDocumentId = :qnum and i.deleted is null", DocumentStructure.class);
        allDocsQ.setParameter("qnum", doc.getId());
        return allDocsQ.getResultList();
    }
}
