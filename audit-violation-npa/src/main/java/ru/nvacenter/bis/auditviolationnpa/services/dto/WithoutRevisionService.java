package ru.nvacenter.bis.auditviolationnpa.services.dto;

import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPA;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPAStructure;
import ru.nvacenter.bis.auditviolationcommon.server.domain.ViolationData;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.WithoutRevAsyncData;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.WithoutRevision;
import ru.nvacenter.bis.auditviolationnpa.services.NpaViolationAnalyseService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.DateStructureService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.UniqueStructureService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.ViolationsService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dmihaylov on 04.04.2018.
 */
@Service
public class WithoutRevisionService {
    @Inject
    ViolationsService violationsService;
    @Inject
    UniqueStructureService uniqueStructureService;
    @Inject
    AsyncDataService asyncDataService;
    @Inject
    DateStructureService dateStructureService;
    @Inject
    NpaViolationAnalyseService npaViolationAnalyseService;

    private CompletableFuture<List<WithoutRevision>> future;
    private ExecutorService delegatedExecutor;
    private WithoutRevAsyncData wrData;
    private List<WithoutRevision> withoutRevisionList = new ArrayList<>();

    public WithoutRevAsyncData create(Optional<String> id) {
        WithoutRevAsyncData data = null;
        if (id.isPresent()){
            UUID idu = UUID.fromString(id.get());
            data = asyncDataService.findWR(idu);
            return data;
        }
        data = asyncDataService.findWR();
        if(data != null)
            return data;
        wrData = asyncDataService.createWR();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        delegatedExecutor = Executors.newFixedThreadPool(3);
        Executor delegatingExecutor =
                new DelegatingSecurityContextExecutor(delegatedExecutor, securityContext);
        future = CompletableFuture.supplyAsync(() -> createList(), delegatingExecutor)
                .whenComplete((ar, e) ->
                {
                    if (e == null) {
                        asyncDataService.finishWR(ar);
                    }
                    else
                    {
                        e.printStackTrace();
                        asyncDataService.finishWithErrorWR(e);
                    }
                    delegatedExecutor.shutdown();
                });

        return wrData;
    }

    private List<WithoutRevision> createList() {
        withoutRevisionList.clear();
        List<NVA_SPR_FKAU_ViolationNPAStructure> listViolStruct = violationsService.findAllWithoutRevNPA();
        int index = 1;
        for(NVA_SPR_FKAU_ViolationNPAStructure violStruct : listViolStruct) {
            if(wrData.getIsCanceled())
                break;
            wrData.setStatus(String.format("Обработано ссылок: %d из %d", index++, listViolStruct.size()));
            NVA_SPR_FKAU_ViolationNPA npa = violationsService.findLinkNpa(violStruct.getIdNPA());
            Long id = 0L;
            if(violStruct.getIdRevisionStructure() != null)
                id = uniqueStructureService.tryFindRevision(npa.getIdSource(), violStruct.getIdRevisionStructure());
            else
                id = uniqueStructureService.tryFindRevision(npa.getIdSource(), violStruct.getIdSource());
            if(id != null) {
                if(wrData.getIsCanceled())
                    break;
                ViolationData vd = violationsService.find(npa.getIdViolation());
                withoutRevisionList.add(createWithoutRevision(npa, violStruct, vd, id));
            }
        }
        return withoutRevisionList;
    }

    private WithoutRevision createWithoutRevision(NVA_SPR_FKAU_ViolationNPA npa, NVA_SPR_FKAU_ViolationNPAStructure vnpas, ViolationData vd, Long idRevs) {
        WithoutRevision withoutRev = new WithoutRevision();
        withoutRev.setViolationText(vd.getViolationText());
        withoutRev.setCaption(npa.getCaption());
        withoutRev.setDate(npa.getDate());
        withoutRev.setNum(npa.getNumber());
        withoutRev.setId(vnpas.getId());
        withoutRev.setIdNpa(vnpas.getIdNPA());
        withoutRev.setText(vnpas.getText());
        withoutRev.setIdRevision(idRevs);
        return withoutRev;
    }

    public void closeThread() {
        future.cancel(true);
        wrData.setIsCanceled(future.isCancelled());
        asyncDataService.finishWR(withoutRevisionList);
        if(!delegatedExecutor.isShutdown())
            delegatedExecutor.shutdown();
    }
}
