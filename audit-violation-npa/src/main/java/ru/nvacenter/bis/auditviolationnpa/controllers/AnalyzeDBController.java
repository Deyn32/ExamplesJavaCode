/**
 * Created by dmihaylov on 14.05.2018.
 */
package ru.nvacenter.bis.auditviolationnpa.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.auditviolationcommon.server.domain.ViolationData;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.AnalyseAsyncData;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.AnalyseResult;
import ru.nvacenter.bis.auditviolationnpa.domain.dto.UnloadedNPA;
import ru.nvacenter.bis.auditviolationnpa.services.NpaViolationAnalyseService;
import ru.nvacenter.bis.auditviolationnpa.services.dao.ViolationsService;
import ru.nvacenter.bis.auditviolationnpa.services.dto.AsyncDataService;
import ru.nvacenter.bis.auditviolationnpa.services.dto.CreateListUnloadedNPAService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@Named("AnalyzeDBController")
public class AnalyzeDBController {
    @Inject
    AsyncDataService asyncDataService;
    @Inject
    NpaViolationAnalyseService npaViolationAnalyseService;
    @Inject
    FZDocumentService fzDocumentService;
    @Inject
    ViolationsService violationsService;

    private ExecutorService delegatedExecutor;
    private AnalyseAsyncData analyseData;
    private CompletableFuture<List<AnalyseResult>> future;

    public CompletableFuture<List<AnalyseResult>> getFuture() {
        return future;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(path = {"npaStartAnalyse"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public AnalyseAsyncData uniqueElementAnalyse(@RequestParam("id") Optional<String> id, @RequestParam("npa") Optional<Long> npa, @RequestParam("viol") Optional<Long> viol) throws Exception{

        AnalyseAsyncData data = null;
        if (id.isPresent()){
            UUID idu = UUID.fromString(id.get());
            data = asyncDataService.find(idu);
            return data;
        }
        data = asyncDataService.find();
        if (data != null) return null;
        analyseData  = asyncDataService.create();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        delegatedExecutor  = Executors.newFixedThreadPool(3);
        Executor delegatingExecutor =
                new DelegatingSecurityContextExecutor(delegatedExecutor, securityContext);

        future = CompletableFuture.supplyAsync(() -> npaViolationAnalyseService.analyseAll2(analyseData, npa, viol), delegatingExecutor)
                .whenComplete((ar, e) ->
                {
                    if (e == null) {
                        asyncDataService.finish(ar);
                    }
                    else
                    {
                        e.printStackTrace();
                        asyncDataService.finishWithError(e);
                    }
                    delegatedExecutor.shutdown();
                });
        return analyseData;
    }

    @RequestMapping(path = {"npaStopAnalyse"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void stopAnalyse() throws Exception {
        future.cancel(true);
        analyseData.setIsCanceled(future.isCancelled());
        asyncDataService.finish(npaViolationAnalyseService.getListResult());
        if (!delegatedExecutor.isShutdown()) {
            delegatedExecutor.shutdown();
        }
    }

    @RequestMapping(path = {"repairLink"},
            method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public boolean repairLink(@RequestBody Long id){
        return npaViolationAnalyseService.repair(id);
    }

    @RequestMapping(path = {"clearQueue"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<AnalyseAsyncData> clearQueue(){
        return asyncDataService.clear();
    }

    @RequestMapping(path = {"findNpa"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<NVA_SPR_AUD_NPA> findAllLinks() {
        return fzDocumentService.getList();
    }

    @RequestMapping(path = {"findAllViol"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<ViolationData> findAllViolations() {
        return violationsService.findAllViol();
    }

    @RequestMapping(path = {"downloadManual"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    //Скачивание пользовательского мануала
    public void downloadManual(HttpServletResponse response) throws Exception {
        String fileName = "Manual.docx";
        response = initResponse(fileName, response);

        try(InputStream input = this.getClass().getClassLoader().getResourceAsStream("META-INF/docs/" + fileName)) {
            FileCopyUtils.copy(input, response.getOutputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HttpServletResponse initResponse(String fileName, HttpServletResponse resp) throws Exception {
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        resp.setContentType(mimeType);

        resp.addHeader("Content-Disposition",
                // если pdf то попробует открыть средствами просмотра браузера
                "inline; "
                        // в этом параметре только в кодировке ISO-8859-1!
                        + "filename=\"" + fileName + "\"; "
                        // для IE 8 (filename*=UTF-8'') не работает
                        // Современные браузеры должны поддерживать https://tools.ietf.org/html/rfc6266#section-4.3
                        + "filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8").replace("+", "%20"));
        return resp;

    }

}
