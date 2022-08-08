package ru.nvacenter.bis.auditviolationnpa.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.nvacenter.bis.audit.npa.domain.dto.compare.NPARevisionCompareNode;
import ru.nvacenter.bis.auditviolationnpa.services.dao.DBRepairService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.DateViolationService;
import ru.nvacenter.bis.auditviolationnpa.services.dto.UniqueCreateStructureService;
import ru.nvacenter.bis.auditviolationnpa.services.dto.ViolationTextStateService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmihaylov on 14.05.2018.
 */

@Controller
@Named("OtherController")
public class OtherController {

    @Inject
    DBRepairService dbRepairService;
    @Inject
    UniqueCreateStructureService uniqueCreateStructureService;
    @Inject
    DateViolationService dateViolationService;

    @RequestMapping(path = {"repairDB"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void repairDB(){
        dbRepairService.create();
    }

    @RequestMapping(path = {"checkUniques"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void checkUniques(String idStr, Long idRev, Long idNPA){
        uniqueCreateStructureService.createOrUpdateUniqueElements(idStr, idRev, idNPA);
    }

    @RequestMapping(path = {"correctDate"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void correctDate(Long idViolation){
        List<Long> listId = new ArrayList<>();
        listId.add(idViolation);
        dateViolationService.updateDateViolation(listId);
    }

    @RequestMapping(path = {"correctAllDate"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void correctAllDate(List<Long> listIdViolations) {
        dateViolationService.updateDateViolation(listIdViolations);
    }
}
