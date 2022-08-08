package ru.nvacenter.bis.npa.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nvacenter.bis.npa.domain.dto.NPAParseResult;
import ru.nvacenter.bis.npa.services.dto.NPADocConverterService;
import ru.nvacenter.bis.npa.services.dto.NPADocTxtParserService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.annotation.MultipartConfig;
import java.io.*;

@Controller
@Named("JspController")
@MultipartConfig(fileSizeThreshold = 20971520)
public class JspController  {

    @Inject
    NPADocTxtParserService npaDocTxtParserService;
    @Inject
    NPADocConverterService npaConvert;


    @RequestMapping(value = {"/audit-npa.html"}, method = RequestMethod.GET)
    public String getIndexView() {
        return "audit-npa";
    }

    @RequestMapping(value = {"/npaparse/uploadfile"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public NPAParseResult parseFile(MultipartFile uploadedFileRef) throws IOException {
        InputStream is = uploadedFileRef.getInputStream();
        //задаем начальную кодировку для сравнения
        NPAParseResult parsRes = npaDocTxtParserService.parseStream(is, "Cp1251");
        //Финальная кодировка
        String code = npaConvert.definitionEncoding(parsRes);
        if (code == "UTF-8") {
            InputStream fin = uploadedFileRef.getInputStream();
            parsRes = npaDocTxtParserService.parseStream(fin, code);
        }
        npaConvert.setParseResult(parsRes);
        return parsRes;
    }



}
