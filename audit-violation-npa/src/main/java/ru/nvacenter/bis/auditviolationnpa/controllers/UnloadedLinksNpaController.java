package ru.nvacenter.bis.auditviolationnpa.controllers;

/**
 * Created by dmihaylov on 14.05.2018.
 */

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.UnloadedNPA;
import ru.nvacenter.bis.auditviolationnpa.services.dto.CreateListUnloadedNPAService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Controller
@Named("UnloadedLinksNpaController")
public class UnloadedLinksNpaController {
    @Inject
    CreateListUnloadedNPAService createListUnloadedNPAService;

    @RequestMapping(path = {"unloadLinksNpa"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<UnloadedNPA> listUnloadLinksNpa() {
        return createListUnloadedNPAService.create();
    }
}
