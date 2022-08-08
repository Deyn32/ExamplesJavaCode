package ru.nvacenter.bis.npa.services.dto;

import org.springframework.stereotype.Service;
import ru.nvacenter.bis.audit.npa.domain.dao.*;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_OrganizationService;
import ru.nvacenter.bis.npa.domain.dto.DocumentNpa;
import ru.nvacenter.bis.npa.domain.dto.RevisionNpa;
import ru.nvacenter.bis.npa.domain.dto.StateDownloadNpa;
import ru.nvacenter.bis.npa.services.dao.SaveDBService;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dmihaylov on 04.06.2018.
 */

@Service
public class DownloadDocumentService {
    @Inject
    CheckService checkService;
    @Inject
    FZDocumentService fzDocumentService;
    @Inject
    SaveDBService saveDBService;
    @Inject
    NVA_SPR_AUD_OrganizationService nva_spr_aud_OrganizationService;
    @Inject
    ZipArchiveService zipArchiveService;
    @Inject
    XmlSerializeService xmlSerializeService;

    private List<DocumentNpa> listDownloadNpa;
    private List<StateDownloadNpa> listState;

    public List<StateDownloadNpa> getListState() { return  listState; }

    public void saveArchive(InputStream is){
        List<DocumentNpa> npas = openArchive(is);
        List<NVA_SPR_AUD_Organization> listOrgs = xmlSerializeService.getOrgs();
        listState = new ArrayList<>();
        for(DocumentNpa npa : npas){
            checkService.checkDicts(npa, listOrgs);
        }
        saveListNpa(npas);
    }

    private List<DocumentNpa> openArchive(InputStream is){
        Map<String, String> xmlMaps = new HashMap<>();
        List<DocumentNpa> listDocs = null;
        try {
            xmlMaps.putAll(zipArchiveService.unZip(is));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            listDocs = xmlSerializeService.deserializeFromXml(xmlMaps);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  listDocs;
    }

    private void saveListNpa(List<DocumentNpa> npas){
        listDownloadNpa = new ArrayList<>();
        listDownloadNpa.addAll(npas);
        npas.forEach(npa -> recursiveBypassNpa(npa));
    }

    private void recursiveBypassNpa(DocumentNpa npa){
        DocumentNpa newNpa = null;
        if(npa.getIdOwner() != null){
            if(checkService.isCheckDownloadNpa(npa.getIdOwner(), listDownloadNpa)){
                newNpa = findNpaFromList(npa.getIdOwner());
                recursiveBypassNpa(newNpa);
            }
        }
        if(npa.getIdParent() != null){
            if(checkService.isCheckDownloadNpa(npa.getIdParent(), listDownloadNpa)){
                newNpa = findNpaFromList(npa.getIdParent());
                recursiveBypassNpa(newNpa);
            }
        }
        for (RevisionNpa downloadRev : npa.getRevisions()){
            if(downloadRev.getId_NPA_Revision() != null) {
                if(checkService.isCheckDownloadNpa(downloadRev.getId_NPA_Revision(), listDownloadNpa)){
                    newNpa = findNpaFromList(downloadRev.getId_NPA_Revision());
                    recursiveBypassNpa(newNpa);
                }
            }
        }
        findNpaInDB(npa);
    }

    private void findNpaInDB(DocumentNpa npa){
        List<NVA_SPR_AUD_NPA> servNpas = fzDocumentService.getListNPA();
        boolean check = true;
        for(NVA_SPR_AUD_NPA servNpa : servNpas){
            if(isCompareNpa(servNpa, npa)){
                check = false;
                replaceId(npa, servNpa.getId());
                StateDownloadNpa stateDownloadNpa = new StateDownloadNpa(npa);
                stateDownloadNpa.setStatus("Есть в БД");
                listState.add(stateDownloadNpa);
                break;
            }
        }
        if(check)
            saveDB(npa);
    }

    private void saveDB(DocumentNpa npa){
        //Сначала сохраняем НПА и получаем новый id
        Long newId = saveDBService.saveNpa(npa);
        if(newId != null){
            replaceId(npa, newId);
        }
        StateDownloadNpa stateDownloadNpa = new StateDownloadNpa(npa);
        stateDownloadNpa.setStatus("Сохранено в БД");
        listState.add(stateDownloadNpa);
        npa.getRevisions().forEach(rev -> {
            Long newIdRev = saveDBService.saveNpaRevisions(rev);
            if(newIdRev != null){
                rev.setId(newIdRev);
                List<NVA_SPR_AUD_NPA_STRUCTURE> listStruct = rev.getListStruct();
                listStruct.forEach(struct -> {
                    struct.setId_SprAudNPAStr(rev.getId());
                    String newIdStruct = saveDBService.saveStructsRev(struct);
                    struct.setId(newIdStruct);
                });
            }
        });
    }

    private void replaceId(DocumentNpa npa, Long newId){
        npa.setId(newId);
        npa.getRevisions().forEach(rev -> {
            rev.setId_NPA(newId);
            rev.getListStruct().forEach(struct -> struct.setId_SprAudNPA(newId));
        });
    }

    private DocumentNpa findNpaFromList(Long id){
        DocumentNpa newNpa = new DocumentNpa();
        for(DocumentNpa parentNpa : listDownloadNpa){
            if(id == parentNpa.getId()){
                newNpa = parentNpa;
                break;
            }
        }
        return newNpa;
    }

    private boolean isCompareNpa(NVA_SPR_AUD_NPA servNpa, DocumentNpa npa){
        if(checkService.equalsName(npa.getNumber(), servNpa.getNumber()) &&
           checkService.equalsName(npa.getType().getName(), servNpa.getType().getName()) &&
           checkService.equalsName(npa.getName(), servNpa.getName())) {
            return true;
        }
        return false;
    }
}
