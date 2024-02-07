package ru.nvacenter.bis.npa.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_Element_Type;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_STRUCTURE;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_RevisionService;
import ru.nvacenter.bis.npa.domain.dto.NPARevisionCompareNode;
import ru.nvacenter.bis.npa.domain.dto.doca.NPA_AUD_ForCopy;
import ru.nvacenter.bis.npa.domain.dto.doca.NPA_AUD_Res;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPANode;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPA_AUD_ContentNode;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPA_AUD_Structure;
import ru.nvacenter.bis.npa.domain.dto.revisions.NPA_AUD_RevNpaData;
import ru.nvacenter.bis.npa.domain.dto.revisions.NPA_AUD_Rev_Data;
import ru.nvacenter.bis.npa.services.dao.FZDocumentCopyService;
import ru.nvacenter.bis.npa.services.dto.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by rGolovin on 27.07.2016.
 */
@RestController
@RequestMapping("/directory")
public class DocFZDirectoryController {

    public DocFZDirectoryController(FZDocumentService fzDocumentService, FZDocumentCopyService fzDocumentCopyService, NVA_SPR_AUD_RevisionService fzRevisionService, NPADocConverterService npaDocConvertService, HtmlNPAViewService htmlFZViewService, NPACheckIsDeleted npaCheckIsdeleted, NPALinearRecursiveService npaLinearRecursiveService, NPARevisionCompareService npaRevisionCompareService, NVA_SPR_AUD_NPAService nvaSprAudNpaService) {
        this.fzDocumentService = fzDocumentService;
        this.fzDocumentCopyService = fzDocumentCopyService;
        this.fzRevisionService = fzRevisionService;
        this.npaDocConvertService = npaDocConvertService;
        this.htmlFZViewService = htmlFZViewService;
        this.npaCheckIsdeleted = npaCheckIsdeleted;
        this.npaLinearRecursiveService = npaLinearRecursiveService;
        this.npaRevisionCompareService = npaRevisionCompareService;
        this.nvaSprAudNpaService = nvaSprAudNpaService;
    }

    private final FZDocumentService fzDocumentService;
    private final FZDocumentCopyService fzDocumentCopyService;
    private final NVA_SPR_AUD_RevisionService fzRevisionService;
    private final NPADocConverterService npaDocConvertService;
    private final HtmlNPAViewService htmlFZViewService;
    private final NPACheckIsDeleted npaCheckIsdeleted;
    private final NPALinearRecursiveService npaLinearRecursiveService;
    private final NPARevisionCompareService npaRevisionCompareService;
    private final NVA_SPR_AUD_NPAService nvaSprAudNpaService;

    /**
     * oshesternikova: для загрузки НПА из "Доки"
     */
    @RequestMapping(path = {"fzCheckDoc"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    //Проверка на существование документа из базы ФОИС по номеру и дате документа
    public NPA_AUD_Res checkIfExists(@RequestParam("num") String num, @RequestParam("dt") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate dt) throws Exception{
        return fzDocumentCopyService.checkIfExists(num, dt);
    }

    @RequestMapping(path = {"fzConvertDoc"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    //перенести документ из ДОКи (idD) в НСИ (id)
    public List<String> tryConvertDoc(@RequestParam("id") Long id, @RequestParam("idD") String idD, @RequestParam("idRev") Optional<Long> idRev) throws Exception{
        return fzDocumentCopyService.tryFindAndConvert(id, idD, idRev);
    }

    @RequestMapping(path = {"fzFindList"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    //Список документов, которые стоят в очереди на копирование в НСИ
    public List<NPA_AUD_ForCopy> findList() throws Exception{
        return fzDocumentCopyService.findListForCopy();
    }

    @RequestMapping(path = {"fzList"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<NPA_AUD_Rev_Data> findListReady() throws Exception{
        return fzDocumentCopyService.findList();
    }

    @RequestMapping(path = {"fzAddNPA"},
            method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public Long addNPA(@RequestBody NVA_SPR_AUD_NPA npa) throws Exception{
        nvaSprAudNpaService.create(npa);
        return npa.getId();
    }

    @RequestMapping(path = {"fzAddNPA/{documentId}"},
            method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateNPA(@PathVariable("documentId") Long documentId, @RequestBody NVA_SPR_AUD_NPA npa) throws Exception{
        nvaSprAudNpaService.update(npa, documentId);
    }

    @RequestMapping(path = {"fzAddNPA/{documentId}"},
            method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteNPA(@PathVariable("documentId") Long documentId) throws Exception{
        nvaSprAudNpaService.delete(documentId);
    }

    @RequestMapping(path = {"fzRev"},
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void addRev(@RequestBody NVA_SPR_AUD_NPA_Revision npa) throws Exception{
        fzRevisionService.create(npa);
    }

    @RequestMapping(path = {"fzRev/{documentId}"},
            method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateRev(@PathVariable("documentId") Long documentId, @RequestBody NVA_SPR_AUD_NPA_Revision npa) throws Exception{
        fzRevisionService.update(npa, documentId);
    }

    @RequestMapping(path = {"fzRev/{documentId}"},
            method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteRev(@PathVariable("documentId") Long documentId) throws Exception{
        fzRevisionService.delete(documentId);
    }


    @RequestMapping(path = {"fzRevList/{documentId}"},
            method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<NPA_AUD_RevNpaData> findRevList(@PathVariable("documentId") Long documentId) throws Exception{
        return fzDocumentCopyService.findRevisions(documentId);
    }

    @RequestMapping(path = {"fzDocumentStructureTree/{documentId}/{revisionId}", "fzDocumentStructureTree/{documentId}"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    //Возращает перечень структурных единиц НПА
    public List<NPA_AUD_ContentNode> getDocumentTreeStructure(
            @PathVariable("documentId") String documentId, @PathVariable("revisionId") Optional<Long> revisionId, @RequestParam("dtRev") @DateTimeFormat(pattern="yyyy-MM-dd") Optional<LocalDate> dateRevision) throws Exception{

        return fzDocumentCopyService.getTreeStructure(documentId, revisionId, dateRevision);
    }

    @RequestMapping(path = {"fzDocStrText/{strId}/{documentId}/{revisionId}", "fzDocStrText/{strId}/{documentId}" },
            method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    //Возращает перечень структурных единиц НПА
    public String getDocumentTreeStructureText(
            @PathVariable("strId") String strId, @PathVariable("documentId") Long documentId, @PathVariable("revisionId") Optional<Long> revisionId) throws Exception{

        return fzDocumentCopyService.getTreeStructureText(strId, documentId, revisionId);
    }

    @RequestMapping(path = {"fzDocStrDates/{strId}/{documentId}/{revisionId}", "fzDocStrDates/{strId}/{documentId}" },
            method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    //Возращает перечень структурных единиц НПА
    public void updateDocumentTreeStructureDate(@PathVariable("strId") String strId, @PathVariable("documentId") Long documentId, @PathVariable("revisionId") Optional<Long> revisionId,
            @RequestBody NPA_AUD_Structure str) throws Exception{
                fzDocumentCopyService.updateDocStructureDates(str, strId, documentId, revisionId);
    }

    @RequestMapping(path = {"fzSaveUploadFile/{documentId}/{revisionId}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    //Сохранение загруженного документа в базу
    public void saveUploadFile(@PathVariable("documentId") Long documentId, @PathVariable("revisionId") Optional<Long> revisionId) {
        npaDocConvertService.tryFindAndConvertDoc(documentId, revisionId);
    }

    @RequestMapping(path = {"fzView/{documentId}/{revisionId}"}, method = RequestMethod.GET, produces = "text/html; charset=windows-1251")
    @ResponseStatus(value = HttpStatus.OK)
    //Возвращает документ редакции
    public String getDocView(@PathVariable("documentId")Long documentId, @PathVariable("revisionId")Optional<Long> revisionId){
        List<NVA_SPR_AUD_NPA_STRUCTURE> strs = fzDocumentService.getListStructure(documentId.toString(), revisionId, null);
        byte[] arr = htmlFZViewService.create(strs, null, null);
        return new String(arr);
    }

    @RequestMapping(path = {"fzCheckIsDeletedDoc/{documentId}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    /* Проверка документа на удаление*/
    public boolean checkIsDeletedDoc(@PathVariable("documentId")Long documentId) {
        return npaCheckIsdeleted.toCheckDoc(documentId);
    }

    @RequestMapping(path = {"fzCheckIsDeletedRev/{documentId}/{revisionId}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    /*Проверка редакции на удаление*/
    public boolean checkIsDeletedRev(@PathVariable("documentId") Long docId, @PathVariable("revisionId")Optional<Long> revisionId) {
        if (revisionId.get() != 0L)
            return npaCheckIsdeleted.toCheckRev(revisionId.get());
        NVA_SPR_AUD_NPA npa = nvaSprAudNpaService.find(docId);
        return  npa.getDeleted();
    }

    @RequestMapping(path = {"fzMergeRev"},
            method = RequestMethod.GET, produces = "text/html; charset=windows-1251")
    @ResponseStatus(value = HttpStatus.OK)
    public String mergeRevisions(@RequestParam("docId") Long docId, @RequestParam("revId1") Optional<Long> revId1, @RequestParam("revId2") Optional<Long> revId2){
        return htmlFZViewService.createMergeView(fzDocumentCopyService.merged(docId, revId1, revId2));
    }

    @RequestMapping(path = {"fzcompareStruct"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public NPARevisionCompareNode compareStruct(@RequestParam("docId") Long docId, @RequestParam("revId1") Optional<Long> revId1, @RequestParam("revId2") Optional<Long> revId2){
        return npaRevisionCompareService.filterCompareNode(fzDocumentCopyService.merged(docId, revId1, revId2));
    }



    @RequestMapping(path = {"fzElemTypes"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    //Возвращает справочник структурных элементов НПА
    public List<NVA_SPR_AUD_Element_Type> getElemTypes() throws Exception{
        return fzDocumentCopyService.getElementTypes();
    }

    @RequestMapping(path = {"fzUpdateElem"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    //Добавление в справичник
    public void updateElem(@RequestBody String name) throws Exception{
        fzDocumentCopyService.updateElem(name);
    }



    @RequestMapping(path = {"fzDownloadManual"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    //Скачивание пользовательского мануала
    public void downloadManual(HttpServletResponse response) throws Exception {
        String fileName = "UserManual.docx";
        response = fzDocumentCopyService.initResponse(fileName, response);

        try(InputStream input = this.getClass().getClassLoader().getResourceAsStream("META-INF/docs/" + fileName)) {
            FileCopyUtils.copy(input, response.getOutputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
