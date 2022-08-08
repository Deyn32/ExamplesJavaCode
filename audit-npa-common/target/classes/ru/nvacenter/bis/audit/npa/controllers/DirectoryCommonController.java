package ru.nvacenter.bis.audit.npa.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.nvacenter.bis.audit.npa.domain.*;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_RevisionService;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by rGolovin on 27.07.2016.
 */
@Controller
@RequestMapping("/directory")
public class DirectoryCommonController {

    @Inject
    FZDocumentService fzDocumentService;

    @RequestMapping(path = {"fzDocTypes"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    //Возращает справочник типов НПА
    public List<NVA_SPR_AUD_Document_Type> getDocTypes() throws Exception{
        List<NVA_SPR_AUD_Document_Type> result = fzDocumentService.getListDocTypes();
        return result;
    }
}
