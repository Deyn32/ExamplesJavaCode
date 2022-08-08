package ru.nvacenter.bis.npa.services.dao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nvacenter.bis.audit.npa.domain.dao.*;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.npa.domain.dto.DocumentNpa;
import ru.nvacenter.bis.npa.domain.dto.RevisionNpa;
import ru.nvacenter.bis.npa.services.dto.DownloadDocumentService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by dmihaylov on 06.06.2018.
 */

@Service
@Transactional(value="nsiTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class SaveDBService {
    @PersistenceContext(unitName = "nsiPersistenceUnit")
    private EntityManager entityManager;
    @Inject
    FZDocumentService fzDocumentService;
    @Inject
    DownloadDocumentService downloadDocumentService;

    public Long saveNpa(DocumentNpa npa){
        NVA_SPR_AUD_NPA servNpa = new NVA_SPR_AUD_NPA();
        servNpa.setIdParent(npa.getIdParent());
        servNpa.setPrevId(npa.getPrevId());
        servNpa.setNumber(npa.getNumber());
        servNpa.setDate(npa.getDate());
        servNpa.setDateEnd(npa.getDateEnd());
        servNpa.setDeleted(npa.getDeleted());
        servNpa.setEditing(npa.getEditing());
        servNpa.setIdOwner(npa.getIdOwner());
        servNpa.setName(npa.getName());
        servNpa.setType(npa.getType());
        servNpa.setSource(npa.getSource());
        servNpa.setShortName(npa.getShortName());
        save(servNpa);
        return servNpa.getId();
    }

    public Long saveNpaRevisions(RevisionNpa rev){
        NVA_SPR_AUD_NPA_Revision saveRev = new NVA_SPR_AUD_NPA_Revision();
        saveRev.setId_NPA(rev.getId_NPA());
        saveRev.setId_NPA_Revision(rev.getId_NPA_Revision());
        saveRev.setDtRev(rev.getDtRev());
        saveRev.setDtBegin(rev.getDtBegin());
        saveRev.setDtEnd(rev.getDtEnd());
        saveRev.setDateModify(rev.getDateModify());
        saveRev.setDeleted(rev.getDeleted());
        save(saveRev);
        return saveRev.getId();
    }

    public String saveStructsRev(NVA_SPR_AUD_NPA_STRUCTURE struct){
        struct.setId(null);
        save(struct);
        return struct.getId();
    }

    public void saveOrg(NVA_SPR_AUD_Organization org){
        org.setId(null);
        save(org);
    }

    public  void saveDocType(NVA_SPR_AUD_Document_Type type){
        type.setId(null);
        save(type);
    }

    private void save(Object object){
        try{
            entityManager.persist(object);
            entityManager.flush();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
