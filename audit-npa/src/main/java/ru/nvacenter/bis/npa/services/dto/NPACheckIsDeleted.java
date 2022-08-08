package ru.nvacenter.bis.npa.services.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.services.dao.FZDocumentService;
import ru.nvacenter.bis.audit.npa.services.dao.NVA_SPR_AUD_NPAService;

/**
 * Created by dmihaylov on 23.08.2017.
 */
@Service
public class NPACheckIsDeleted {

    @Autowired
    private FZDocumentService fzDocumentService;
    @Autowired
    NVA_SPR_AUD_NPAService nva_SPR_AUD_NPAService;
    public boolean toCheckDoc(Long id) {
        NVA_SPR_AUD_NPA npa = nva_SPR_AUD_NPAService.find(id);
        boolean owner = npa.getDeleted();
        return owner;
    }

    public boolean toCheckRev(Long idRev) {
        NVA_SPR_AUD_NPA_Revision npaRev = fzDocumentService.findRevisionByID(idRev);
        boolean owner = npaRev.getDeleted();
        return owner;
    }
}
