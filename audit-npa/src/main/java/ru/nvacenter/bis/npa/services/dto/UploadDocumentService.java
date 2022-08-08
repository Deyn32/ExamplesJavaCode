package ru.nvacenter.bis.npa.services.dto;

/**
 * Created by dmihaylov on 22.05.2018.
 */

import org.springframework.stereotype.Service;
import ru.nvacenter.bis.audit.npa.domain.dao.*;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_NPAService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_OrganizationService;
import ru.nvacenter.bis.npa.domain.dto.DocumentNpa;
import ru.nvacenter.bis.npa.domain.dto.RevisionNpa;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UploadDocumentService {
    @Inject
    CheckService checkService;
    @Inject
    FZDocumentService fzDocumentService;
    @Inject
    XmlSerializeService xmlSerializeService;
    @Inject
    ZipArchiveService zipArchiveService;
    @Inject
    NVA_SPR_AUD_NPAService nva_SPR_AUD_NPAService;
    @Inject
    NVA_SPR_AUD_OrganizationService nva_spr_aud_OrganizationService;

    private List<NVA_SPR_AUD_NPA> addictionsNpa;
    private List<NVA_SPR_AUD_NPA> mainNpa;

    public byte[] createDocumentNpa(List<Long> ids) throws Exception {
        addictionsNpa = nva_SPR_AUD_NPAService.find(ids);
        mainNpa = new ArrayList<>();
        mainNpa.addAll(addictionsNpa);
        for(NVA_SPR_AUD_NPA npa : mainNpa){
            findAddictionsNpa(npa);
        }
        return archiveCreate(formationDocNpa());
    }

    public byte[] archiveCreate(List<DocumentNpa> npas) throws Exception{
        Map<String, String> xmlMaps = new HashMap<>();
        List<NVA_SPR_AUD_Organization> listOrgs = new ArrayList<>();
        listOrgs.addAll(nva_spr_aud_OrganizationService.getAll());
        xmlMaps.putAll(xmlSerializeService.serialize("Организации.xml", listOrgs));
        npas.forEach( s -> {
            String fileName = s.getNumber() + s.getType().getName() + ".xml";
            try {
                xmlMaps.putAll(xmlSerializeService.serialize(fileName, s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return zipArchiveService.zip(xmlMaps);
    }

    private List<DocumentNpa> formationDocNpa() throws Exception{
        List<DocumentNpa> npaList = new ArrayList<>();
        for(NVA_SPR_AUD_NPA npa : addictionsNpa){
            List<NVA_SPR_AUD_NPA_Revision> revs = fzDocumentService.findRevisionByNpa(npa.getId());
            if (revs.size() != 0) {
                DocumentNpa docNpa = initDocNpa(npa);
                List<NVA_SPR_AUD_Organization_REF_NPA> listOrgsNpa = nva_spr_aud_OrganizationService.getOrgsForNpa(docNpa.getId());
                docNpa.setOrgsNpa(listOrgsNpa);
                formationRevision(docNpa, revs);
                npaList.add(docNpa);
            }
        }
        return npaList;
    }

    private DocumentNpa initDocNpa(NVA_SPR_AUD_NPA npa){
        DocumentNpa docNpa = new DocumentNpa();
        docNpa.setId(npa.getId());
        docNpa.setIdOwner(npa.getIdOwner());
        docNpa.setDate(npa.getDate());
        docNpa.setDeleted(npa.getDeleted());
        docNpa.setDateEnd(npa.getDateEnd());
        docNpa.setEditing(npa.getEditing());
        docNpa.setIdParent(npa.getIdParent());
        docNpa.setName(npa.getName());
        docNpa.setNumber(npa.getNumber());
        docNpa.setPrevId(npa.getPrevId());
        docNpa.setType(npa.getType());
        docNpa.setShortName(npa.getShortName());
        docNpa.setSource(npa.getSource());

        return docNpa;
    }

    private void formationRevision(DocumentNpa docNpa, List<NVA_SPR_AUD_NPA_Revision> revs){
        List<NVA_SPR_AUD_NPA_STRUCTURE> listStruct = fzDocumentService.findStruct(docNpa.getId());
        docNpa.setRevisions(new ArrayList<>());
        revs.forEach(value -> {
            RevisionNpa rev = initRev(value);
            rev.setListStruct(new ArrayList<>());
            listStruct.forEach(val -> {
                if(val.getId_SprAudNPAStr() != null){
                    if(val.getId_SprAudNPAStr().intValue() == value.getId().intValue())
                        rev.getListStruct().add(val);
                }
                else rev.getListStruct().addAll(listStruct);
            });
            docNpa.getRevisions().add(rev);
        });
    }

    private RevisionNpa initRev(NVA_SPR_AUD_NPA_Revision rev){
        RevisionNpa docRev = new RevisionNpa();
        docRev.setId(rev.getId());
        docRev.setDtBegin(rev.getDtBegin());
        docRev.setDtRev(rev.getDtRev());
        docRev.setDtEnd(rev.getDtEnd());
        docRev.setDateModify(rev.getDateModify());
        docRev.setDeleted(rev.getDeleted());
        docRev.setId_NPA_Revision(rev.getId_NPA_Revision());
        docRev.setId_NPA(rev.getId_NPA());
        return docRev;
    }

    private void findAddictionsNpa (NVA_SPR_AUD_NPA npa){
        NVA_SPR_AUD_NPA newNpa = null;
        if(npa.getIdOwner() != null){
            if(checkService.isCheckNpa(npa.getIdOwner(), addictionsNpa)){
                newNpa = nva_SPR_AUD_NPAService.find(npa.getIdOwner());
                addictionsNpa.add(newNpa);
                findAddictionsNpa(newNpa);
            }
        }
        if(npa.getIdParent() != null){
            if(checkService.isCheckNpa(npa.getIdParent(), addictionsNpa)){
                newNpa = nva_SPR_AUD_NPAService.find(npa.getIdParent());
                addictionsNpa.add(newNpa);
                findAddictionsNpa(newNpa);
            }
        }
        List<NVA_SPR_AUD_NPA_Revision> revs = fzDocumentService.findRevisionByNpa(npa.getId());
        for (NVA_SPR_AUD_NPA_Revision rev : revs){
            if(rev.getId_NPA_Revision() != null){
                if(checkService.isCheckNpa(rev.getId_NPA_Revision(), addictionsNpa)){
                    newNpa = nva_SPR_AUD_NPAService.find(rev.getId_NPA_Revision());
                    addictionsNpa.add(newNpa);
                    findAddictionsNpa(newNpa);
                }
            }
        }
    }
}