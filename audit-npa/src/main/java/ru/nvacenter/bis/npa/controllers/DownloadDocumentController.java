package ru.nvacenter.bis.npa.controllers;

/**
 * Created by dmihaylov on 01.06.2018.
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import ru.nvacenter.bis.npa.domain.dto.StateDownloadNpa;
import ru.nvacenter.bis.npa.services.dto.XmlSerializeService;
import ru.nvacenter.bis.npa.services.dto.DownloadDocumentService;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@Named("DownloadDocumentController")
public class DownloadDocumentController {

    @Inject
    XmlSerializeService xmlSerializeService;
    @Inject
    DownloadDocumentService downloadDocumentService;


    @RequestMapping(path = {"downloadNpaZip"},  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<StateDownloadNpa> downloadNpa(MultipartFile uploadedFileRef) throws Exception {
        InputStream is = uploadedFileRef.getInputStream();
        List<StateDownloadNpa> listState = new ArrayList<>();
        downloadDocumentService.saveArchive(is);
        listState.addAll(downloadDocumentService.getListState());
        return listState;
    }
}
