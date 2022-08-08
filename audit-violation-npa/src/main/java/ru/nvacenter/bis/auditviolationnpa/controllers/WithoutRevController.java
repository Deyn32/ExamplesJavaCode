package ru.nvacenter.bis.auditviolationnpa.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.WithoutRevAsyncData;
import ru.nvacenter.bis.auditviolationnpa.models.RepairLink;
import ru.nvacenter.bis.auditviolationnpa.services.NpaViolationAnalyseService;
import ru.nvacenter.bis.auditviolationnpa.services.dto.WithoutRevisionService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
/**
 * Created by dmihaylov on 14.05.2018.
 */

@Controller
@Named("WithoutRevController")
public class WithoutRevController {
    @Inject
    WithoutRevisionService withoutRevisionService;
    @Inject
    NpaViolationAnalyseService npaViolationAnalyseService;

    @RequestMapping(path = {"withoutRev"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public WithoutRevAsyncData listWithoutRevision(@RequestParam("id") Optional<String> id) throws Exception {
        return withoutRevisionService.create(id);
    }

    @RequestMapping(path = {"stopWithoutRev"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void stopAsyncWithoutRev() {
        withoutRevisionService.closeThread();
    }

    @RequestMapping(path = {"repairAllLinks"}, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<RepairLink> repairAllLinks(@RequestBody List<RepairLink> ids) {
        return npaViolationAnalyseService.repairAll(ids);
    }
}
