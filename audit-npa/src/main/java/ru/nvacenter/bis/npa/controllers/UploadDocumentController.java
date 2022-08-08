package ru.nvacenter.bis.npa.controllers;

/**
 * Created by dmihaylov on 22.05.2018.
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.nvacenter.bis.npa.services.dto.XmlSerializeService;
import ru.nvacenter.bis.npa.services.dto.UploadDocumentService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.List;

@Controller
@Named("UploadDocumentController")
public class UploadDocumentController {
    @Inject
    UploadDocumentService uploadDocumentService;
    @Inject
    XmlSerializeService xmlSerializeService;

    @RequestMapping(path = {"createFiles"},
            method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void createFile(@RequestParam("doc") List<Long> ids, HttpServletResponse response) throws Exception{
        String fileName =  LocalDate.now() + ".zip";
        byte[] bytes = uploadDocumentService.createDocumentNpa(ids);
        setResponse(fileName, response, bytes.length);
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            InputStream inputStream = new BufferedInputStream(byteArrayInputStream)) {
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setResponse(String fileName, HttpServletResponse resp, int len) throws  Exception{
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        resp.setContentType(mimeType);
        String headerKey = "Content-Disposition";
        String headerValue = String.format("inline; filename=\"%s\"", fileName);
        resp.addHeader(headerKey, headerValue);
        resp.setContentLength(len);
    }
}
