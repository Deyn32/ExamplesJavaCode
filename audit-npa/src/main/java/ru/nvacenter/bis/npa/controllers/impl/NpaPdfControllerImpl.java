package ru.nvacenter.bis.npa.controllers.impl;

import org.springframework.util.FileCopyUtils;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_STRUCTURE;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.npa.controllers.NpaPdfController;
import ru.nvacenter.bis.npa.services.dao.FZDocumentCopyService;
import ru.nvacenter.bis.npa.services.dto.PdfNPAViewService;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class NpaPdfControllerImpl implements NpaPdfController {

    public NpaPdfControllerImpl(FZDocumentService fzDocumentService, FZDocumentCopyService fzDocumentCopyService, PdfNPAViewService npaPdfService, NVA_SPR_AUD_NPAService nvaSprAudNpaService) {
        this.fzDocumentService = fzDocumentService;
        this.fzDocumentCopyService = fzDocumentCopyService;
        this.npaPdfService = npaPdfService;
        this.nvaSprAudNpaService = nvaSprAudNpaService;
    }

    private final FZDocumentService fzDocumentService;
    private final FZDocumentCopyService fzDocumentCopyService;
    private final PdfNPAViewService npaPdfService;
    private final NVA_SPR_AUD_NPAService nvaSprAudNpaService;

    @Override
    public void download(HttpServletResponse response, Long docId, Optional<Long> revId) throws Exception{
        NVA_SPR_AUD_NPA_Revision rev = revId.map(fzDocumentService::findRevisionByID).orElse(null);
        NVA_SPR_AUD_NPA doc = nvaSprAudNpaService.find(docId);
        List<NVA_SPR_AUD_NPA_STRUCTURE> str1 = fzDocumentService.getListStructure(docId.toString(), revId, Optional.empty());
        byte[] bytes = npaPdfService.create(str1, rev, doc);
        String fileName = "output.pdf";
        response = fzDocumentCopyService.initResponse(fileName, response);
        response.setContentLength(bytes.length);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             InputStream inputStream = new BufferedInputStream(byteArrayInputStream)) {

            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
    }
}
