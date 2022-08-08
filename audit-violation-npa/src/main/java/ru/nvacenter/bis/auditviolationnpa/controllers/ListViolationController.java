package ru.nvacenter.bis.auditviolationnpa.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.nvacenter.bis.audit.npa.domain.dto.NPA_AUD_ContentNode;
import ru.nvacenter.bis.audit.npa.domain.dto.compare.NPARevisionCompareNode;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.AnalyseResultBase;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.RevLinksAsyncData;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.ViolationRevData;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.ViolationTreeData;
import ru.nvacenter.bis.auditviolationnpa.models.SaveState;
import ru.nvacenter.bis.auditviolationnpa.services.NpaViolationAnalyseService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.DBRepairService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.DateViolationService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.ViolationsService;
import ru.nvacenter.bis.auditviolationnpa.services.dto.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dmihaylov on 10.05.2018.
 */

@Controller
@Named("ListViolationController")
public class ListViolationController {

    @Inject
    ViolationsTreeService violationsTreeService;
    @Inject
    DateViolationService dateViolationService;
    @Inject
    NpaViolationAnalyseService npaViolationAnalyseService;
    @Inject
    SaveStateService saveStateService;
    @Inject
    ViolationTextStateService violationTextStateService;
    @Inject
    RegroupingService regroupingService;
    @Inject
    AsyncDataService asyncDataService;
    @Inject
    ViolationsService violationsService;
    @Inject
    DBRepairService dbRepairService;

    private CompletableFuture<List<ViolationRevData>> future;
    private ExecutorService delegatedExecutor;
    private RevLinksAsyncData revLinks;

    @RequestMapping(path = {"emptyLinks"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<AnalyseResultBase> findAllEmpty(){
        return npaViolationAnalyseService.findDoubtful();
    }

    @RequestMapping(path = {"listLinks"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Collection<ViolationTreeData> listLinks(){
        return violationsTreeService.findViolationsAsTree();
    }

    @RequestMapping(path = {"listRevsLinks"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<ViolationRevData> listRevsLinks(Long id){
        if (id == null) return new ArrayList<>();
        return violationsTreeService.revisionsByLink(id);
    }

    @RequestMapping(path = {"detailLinks"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<NPA_AUD_ContentNode> detailLinks(Long id){
        return violationsTreeService.structureAbstractByLink(id);
    }


    @RequestMapping(path = {"detailLinksRev"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<NPA_AUD_ContentNode> detailLinksRev(Long id, Optional<Long> idRev){
        Long idRev2 = idRev.isPresent() ? idRev.get() : null;
        return violationsTreeService.structureAbstractByLink(id, idRev2);
    }

    @RequestMapping(path = {"filterDate"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Collection<ViolationTreeData> filterDate( @RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date) {
        return dateViolationService.greaterOrEquailCurrentDate(date);
    }

    @RequestMapping(path = {"filterDoubtful"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Collection<ViolationTreeData> filterDoubtful( ) {
        return violationsTreeService.findDoubtfulViols();
    }

    @RequestMapping(path = {"fzcompareStructAbs"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public NPARevisionCompareNode compareStructAbs(Long idLink, Long revId1, Long revId2){
        return npaViolationAnalyseService.compareRevAbstract(idLink, revId1, revId2);
    }

    @RequestMapping(path = {"findStatuses"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<String> findStatuses() {
        List<String> statuses = new ArrayList<>();
        for (ViolationRevData.ViolRevState status: ViolationRevData.ViolRevState.values()){
            statuses.add(status.toString());
        }
        return statuses;
    }

    @RequestMapping(path = {"findStructElemStatus"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<String> findStructElemStatus() {
        List<String> statuses = new ArrayList<>();
        for (ViolationRevData.StructElemRevState status: ViolationRevData.StructElemRevState.values()){
            statuses.add(status.toString());
        }
        return statuses;
    }

    @RequestMapping(path = {"saveStatus"},
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public boolean saveStatus(@RequestBody SaveState saveState) {
        if (saveState.getOldState() == saveState.getNewState()) return false;
        return saveStateService.saveState(saveState);
    }

    @RequestMapping(path = {"editDateViol"},
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public boolean editDateViol(@RequestBody ViolationTreeData violationTreeData) {
        return dateViolationService.saveEditDateViol(violationTreeData);
    }

    @RequestMapping(path = {"fzcompareViolAbs"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public NPARevisionCompareNode compareStructAbs(Long idLink, Long revId){
        return violationTextStateService.compare(idLink, revId);
    }

    @RequestMapping(path = {"regrouping"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RevLinksAsyncData regrouping(Optional<String> id, Long idNpaStruct, Long idNpa) {
        RevLinksAsyncData data = null;
        if(id.isPresent()){
            UUID idu = UUID.fromString(id.get());
            data = asyncDataService.findRL(idu);
            return data;
        }
        revLinks = asyncDataService.createRL();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        delegatedExecutor = Executors.newFixedThreadPool(3);
        Executor delegatingExecutor =
                new DelegatingSecurityContextExecutor(delegatedExecutor, securityContext);
        future = CompletableFuture.supplyAsync(() -> regroupingService.regrouping(idNpaStruct, idNpa), delegatingExecutor)
                .whenComplete((ar, e) -> {
                    if (e == null)
                        asyncDataService.finishRL(ar);
                    else {
                        e.printStackTrace();
                        asyncDataService.finishWithErrorRL(e);
                    }
                    delegatedExecutor.shutdown();
                });
        return revLinks;
    }
}
