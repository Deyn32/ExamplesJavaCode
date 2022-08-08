package ru.nvacenter.bis.npa.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_Element_Type;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;
import ru.nvacenter.bis.audit.npa.domain.dto.compare.NPARevisionCompareNode;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_NPAService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_RevisionService;
import ru.nvacenter.bis.audit.npa.services.dto.NPALinearRecursiveService;
import ru.nvacenter.bis.audit.npa.services.dto.NPARevisionCompareService;
import ru.nvacenter.bis.npa.domain.dto.doca.NPA_AUD_ForCopy;
import ru.nvacenter.bis.npa.domain.dto.doca.NPA_AUD_Res;
import ru.nvacenter.bis.audit.npa.domain.dto.NPANode;
import ru.nvacenter.bis.audit.npa.domain.dto.NPA_AUD_ContentNode;
import ru.nvacenter.bis.audit.npa.domain.dto.NPA_AUD_Structure;
import ru.nvacenter.bis.npa.domain.dto.revisions.NPA_AUD_RevNpaData;
import ru.nvacenter.bis.npa.domain.dto.revisions.NPA_AUD_Rev_Data;
import ru.nvacenter.bis.npa.services.dto.*;
import ru.nvacenter.bis.npa.services.dao.FZDocumentCopyService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by rGolovin on 27.07.2016.
 */
@Controller
@RequestMapping("/directory")
public class DocFZDirectoryController {
    @Inject
    FZDocumentService fzDocumentService;
    @Inject
    FZDocumentCopyService fzDocumentCopyService;
    @Inject
    NVA_SPR_AUD_RevisionService fzRevisionService;
    @Inject
    NPADocConverterService npaDocConvertService;
    @Inject
    HtmlNPAViewService htmlFZViewService;
    @Inject
    NPACheckIsDeleted npaCheckIsdeleted;
    @Inject
    NPALinearRecursiveService npaLinearRecursiveService;
    @Inject
    NPARevisionCompareService npaRevisionCompareService;
    @Inject
    PdfNPAViewService npaPdfService;
    @Inject
    NVA_SPR_AUD_NPAService nva_SPR_AUD_NPAService;
    /**
     * oshesternikova: для загрузки НПА из "Доки"
     */
    @RequestMapping(path = {"fzCheckDoc"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //Проверка на существование документа из базы ФОИС по номеру и дате документа
    public NPA_AUD_Res checkIfExists(@RequestParam("num") String num, @RequestParam("dt") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate dt) throws Exception{
        return fzDocumentCopyService.checkIfExists(num, dt);
    }

    @RequestMapping(path = {"fzConvertDoc"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //перенести документ из ДОКи (idD) в НСИ (id)
    public List<String> tryConvertDoc(@RequestParam("id") Long id, @RequestParam("idD") String idD, @RequestParam("idRev") Optional<Long> idRev) throws Exception{
        return fzDocumentCopyService.tryFindAndConvert(id, idD, idRev);
    }

    @RequestMapping(path = {"fzFindList"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //Список документов, которые стоят в очереди на копирование в НСИ
    public List<NPA_AUD_ForCopy> findList() throws Exception{
        return fzDocumentCopyService.findListForCopy();
    }

    @RequestMapping(path = {"fzList"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<NPA_AUD_Rev_Data> findListReady() throws Exception{
        return fzDocumentCopyService.findList();
    }

    @RequestMapping(path = {"fzAddNPA"},
            method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Long addNPA(@RequestBody NVA_SPR_AUD_NPA npa) throws Exception{
        nva_SPR_AUD_NPAService.create(npa);
        return npa.getId();
    }

    @RequestMapping(path = {"fzAddNPA/{documentId}"},
            method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void updateNPA(@PathVariable("documentId") Long documentId, @RequestBody NVA_SPR_AUD_NPA npa) throws Exception{
        nva_SPR_AUD_NPAService.update(npa, documentId);
    }

    @RequestMapping(path = {"fzAddNPA/{documentId}"},
            method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void deleteNPA(@PathVariable("documentId") Long documentId) throws Exception{
        nva_SPR_AUD_NPAService.delete(documentId);
    }

    @RequestMapping(path = {"fzRev"},
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void addRev(@RequestBody NVA_SPR_AUD_NPA_Revision npa) throws Exception{
        fzRevisionService.create(npa);
    }

    @RequestMapping(path = {"fzRev/{documentId}"},
            method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void updateRev(@PathVariable("documentId") Long documentId, @RequestBody NVA_SPR_AUD_NPA_Revision npa) throws Exception{
        fzRevisionService.update(npa, documentId);
    }

    @RequestMapping(path = {"fzRev/{documentId}"},
            method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void deleteRev(@PathVariable("documentId") Long documentId) throws Exception{
        fzRevisionService.delete(documentId);
    }


    @RequestMapping(path = {"fzRevList/{documentId}"},
            method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<NPA_AUD_RevNpaData> findRevList(@PathVariable("documentId") Long documentId) throws Exception{
        return fzDocumentCopyService.findRevisions(documentId);
    }

    @RequestMapping(path = {"fzDocumentStructureTree/{documentId}/{revisionId}", "fzDocumentStructureTree/{documentId}"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //Возращает перечень структурных единиц НПА
    public List<NPA_AUD_ContentNode> getDocumentTreeStructure(
            @PathVariable("documentId") String documentId, @PathVariable("revisionId") Optional<Long> revisionId, @RequestParam("dtRev") @DateTimeFormat(pattern="yyyy-MM-dd") Optional<LocalDate> dateRevision) throws Exception{
        List<NPA_AUD_ContentNode> result = fzDocumentCopyService.getTreeStructure(documentId, revisionId, dateRevision);
        return result;
    }

    @RequestMapping(path = {"fzDocStrText/{strId}/{documentId}/{revisionId}", "fzDocStrText/{strId}/{documentId}" },
            method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //Возращает перечень структурных единиц НПА
    public String getDocumentTreeStructureText(
            @PathVariable("strId") String strId, @PathVariable("documentId") Long documentId, @PathVariable("revisionId") Optional<Long> revisionId) throws Exception{
        String result = fzDocumentCopyService.getTreeStructureText(strId, documentId, revisionId);
        return result;
    }

    @RequestMapping(path = {"fzDocStrDates/{strId}/{documentId}/{revisionId}", "fzDocStrDates/{strId}/{documentId}" },
            method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //Возращает перечень структурных единиц НПА
    public void updateDocumentTreeStructureDate(@PathVariable("strId") String strId, @PathVariable("documentId") Long documentId, @PathVariable("revisionId") Optional<Long> revisionId,
            @RequestBody NPA_AUD_Structure str) throws Exception{
                fzDocumentCopyService.updateDocStructureDates(str, strId, documentId, revisionId);
    }

    @RequestMapping(path = {"fzSaveUploadFile/{documentId}/{revisionId}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //Сохранение загруженного документа в базу
    public void saveUploadFile(@PathVariable("documentId") Long documentId, @PathVariable("revisionId") Optional<Long> revisionId) {
        npaDocConvertService.tryFindAndConvertDoc(documentId, revisionId);
    }

    @RequestMapping(path = {"fzView/{documentId}/{revisionId}"}, method = RequestMethod.GET, produces = "text/html; charset=windows-1251")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //Возвращает документ редакции
    public String getDocView(@PathVariable("documentId")Long documentId, @PathVariable("revisionId")Optional<Long> revisionId){
        List<NVA_SPR_AUD_NPA_STRUCTURE> strs = fzDocumentService.getListStructure(documentId.toString(), revisionId, null);
        byte[] arr = htmlFZViewService.create(strs, null, null);
        return new String(arr);
    }

    @RequestMapping(path = {"fzCheckIsDeletedDoc/{documentId}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    /* Проверка документа на удаление*/
    public boolean checkIsDeletedDoc(@PathVariable("documentId")Long documentId) {
        return npaCheckIsdeleted.toCheckDoc(documentId);
    }

    @RequestMapping(path = {"fzCheckIsDeletedRev/{documentId}/{revisionId}"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    /*Проверка редакции на удаление*/
    public boolean checkIsDeletedRev(@PathVariable("documentId") Long docId, @PathVariable("revisionId")Optional<Long> revisionId) {
        if (revisionId.get() != 0L)
            return npaCheckIsdeleted.toCheckRev(revisionId.get());
        NVA_SPR_AUD_NPA npa = nva_SPR_AUD_NPAService.find(docId);
        return  npa.getDeleted();
    }

    @RequestMapping(path = {"fzMergeRev"},
            method = RequestMethod.GET, produces = "text/html; charset=windows-1251")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String mergeRevisions(@RequestParam("docId") Long docId, @RequestParam("revId1") Optional<Long> revId1, @RequestParam("revId2") Optional<Long> revId2){
        return htmlFZViewService.createMergeView(this.merged(docId, revId1, revId2));
    }


    @RequestMapping(path = {"fzcompareStruct"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public NPARevisionCompareNode compareStruct(@RequestParam("docId") Long docId, @RequestParam("revId1") Optional<Long> revId1, @RequestParam("revId2") Optional<Long> revId2){
        return npaRevisionCompareService.filterCompareNode(this.merged(docId, revId1, revId2));
    }

    private NPARevisionCompareNode merged(Long docId, Optional<Long> revId1, Optional<Long> revId2) {
        List<NVA_SPR_AUD_NPA_STRUCTURE> str1 = fzDocumentService.getListStructure(docId.toString(), revId1, Optional.empty());
        List<NVA_SPR_AUD_NPA_STRUCTURE> str2 = fzDocumentService.getListStructure(docId.toString(), revId2, Optional.empty());
        NPANode rev1 = npaLinearRecursiveService.ToRecursive(str1);
        NPANode rev2 = npaLinearRecursiveService.ToRecursive(str2);
        return npaRevisionCompareService.mergeRoot(rev1, rev2, false);
    }

    @RequestMapping(path = {"fzElemTypes"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //Возвращает справочник структурных элементов НПА
    public List<NVA_SPR_AUD_Element_Type> getElemTypes() throws Exception{
        return fzDocumentCopyService.getElementTypes();
    }

    @RequestMapping(path = {"fzUpdateElem"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //Добавление в справичник
    public void updateElem(@RequestBody String name) throws Exception{
        fzDocumentCopyService.updateElem(name);
    }

    @RequestMapping(path = {"fzDownloadNPA"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    //Добавление в справичник
    public void download(HttpServletResponse response, @RequestParam("docId") Long docId, @RequestParam("revId") Optional<Long> revId) throws Exception{
        NVA_SPR_AUD_NPA_Revision rev = revId.isPresent() ?  fzDocumentService.findRevisionByID(revId.get()) : null;
        NVA_SPR_AUD_NPA doc = nva_SPR_AUD_NPAService.find(docId);
        List<NVA_SPR_AUD_NPA_STRUCTURE> str1 = fzDocumentService.getListStructure(docId.toString(), revId, Optional.empty());
        byte[] bytes = npaPdfService.create(str1, rev, doc);
        String fileName = "output.pdf";
        response = fzDocumentCopyService.initResponse(fileName, response);
        response.setContentLength(bytes.length);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             InputStream inputStream = new BufferedInputStream(byteArrayInputStream)) {

            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }

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
