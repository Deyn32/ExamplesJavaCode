package ru.nvacenter.bis.npa.services.dto;

import org.springframework.stereotype.Service;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_Document_Type;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_Organization;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_Organization_REF_NPA;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_OrganizationService;
import ru.nvacenter.bis.npa.domain.dto.DocumentNpa;
import ru.nvacenter.bis.npa.services.dao.SaveDBService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmihaylov on 19.06.2018.
 */
@Service
public class CheckService {
    @Inject
    FZDocumentService fzDocumentService;
    @Inject
    NVA_SPR_AUD_OrganizationService nva_spr_aud_organizationService;
    @Inject
    SaveDBService saveDBService;

    public boolean isCheckNpa(Long id, List<NVA_SPR_AUD_NPA> addictionsNpa){
        boolean isCheck = true;
        for (NVA_SPR_AUD_NPA npa : addictionsNpa){
            if(npa.getId() == id )
                isCheck = false;
        }
        return isCheck;
    }

    public boolean isCheckDownloadNpa(Long id, List<DocumentNpa> listDownloadNpa){
        DocumentNpa removeNpa = null;
        boolean isCheck = false;
        for (DocumentNpa npa : listDownloadNpa){
            if(npa.getId() == id ){
                isCheck = true;
                removeNpa = npa;
            }
        }
        if(removeNpa != null)
        {
            listDownloadNpa.remove(removeNpa);
        }
        return isCheck;
    }

    public DocumentNpa checkDicts(DocumentNpa npa, List<NVA_SPR_AUD_Organization> downloadOrgs){
        npa = checkTypeNpa(npa);
        if(npa.getOrgsNpa().size() != 0)
            npa = checkOrg(npa, downloadOrgs);
        return npa;
    }

    private DocumentNpa checkOrg(DocumentNpa npa, List<NVA_SPR_AUD_Organization> downloadOrgs) {
        List<NVA_SPR_AUD_Organization_REF_NPA> npaOrgs = new ArrayList<>();
        npaOrgs.addAll(npa.getOrgsNpa());
        for (NVA_SPR_AUD_Organization_REF_NPA organization_ref_npa : npaOrgs){
            for (NVA_SPR_AUD_Organization org : downloadOrgs){
                if(organization_ref_npa.getIdOrganization() == org.getId()){
                    List<NVA_SPR_AUD_Organization> listAllOrgsServ = nva_spr_aud_organizationService.getAll();
                    boolean check = true;
                    for(NVA_SPR_AUD_Organization servOrg : listAllOrgsServ) {
                        if(equalsName(org.getFullName(), servOrg.getFullName())){
                            check = false;
                            if(servOrg.getId() != org.getId()){
                                org.setId(servOrg.getId());
                                organization_ref_npa.setId(servOrg.getId());
                            }
                            break;
                        }
                    }
                    if(check){
                        saveDBService.saveOrg(org);
                        List<NVA_SPR_AUD_Organization> listOrgsSer = nva_spr_aud_organizationService.getAll();
                        listOrgsSer.forEach(servOrganization -> {
                            if(equalsName(org.getFullName(), servOrganization.getFullName())){
                                organization_ref_npa.setIdOrganization(org.getId());
                            }
                        });
                    }
                }
            }
        }
        npa.getOrgsNpa().clear();
        npa.setOrgsNpa(npaOrgs);
        return npa;
    }

    private DocumentNpa checkTypeNpa(DocumentNpa npa){
        List<NVA_SPR_AUD_Document_Type> listServDocType = fzDocumentService.getListDocTypes();
        boolean check = true;
        for(NVA_SPR_AUD_Document_Type docType : listServDocType){
            if(equalsName(npa.getType().getName(), docType.getName())){
                check = false;
                if(docType.getId() != npa.getType().getId()){
                    npa.getType().setId(docType.getId());
                }
                break;
            }
        }
        if(check){
            npa = saveBase(npa);
        }
        return npa;
    }

    private DocumentNpa saveBase(DocumentNpa npa){
        saveDBService.saveDocType(npa.getType());
        NVA_SPR_AUD_Document_Type type = fzDocumentService.getDocumentType(npa.getType().getName());
        npa.setType(type);
        return npa;
    }

    public boolean equalsName(String objectName, String servName){
        String[] arrayNpaNameDocType = objectName.split(" ");
        String[] arrayNameDocType = servName.split(" ");
        if(arrayNpaNameDocType.length != arrayNameDocType.length) return false;
        for(int i = 0; i < arrayNpaNameDocType.length; i++){
            arrayNpaNameDocType[i] = arrayNpaNameDocType[i].toLowerCase();
            arrayNameDocType[i] = arrayNameDocType[i].toLowerCase();
            if(!arrayNpaNameDocType[i].equals(arrayNameDocType[i])){
                return false;
            }
        }
        return true;
    }
}
