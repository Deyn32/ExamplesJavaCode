package ru.nvacenter.bis.npa.domain.dto.revisions;

import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_Revision;

/**
 * Created by oshesternikova on 11.04.2017.
 * Кнтракт для передачи данных по редакции документа
 */
public class NPA_AUD_RevNpaData {
    public NPA_AUD_RevNpaData(NVA_SPR_AUD_NPA_Revision rev, Boolean isLoaded) {
        this.rev = rev;
        this.isLoaded = isLoaded;
    }

    public NVA_SPR_AUD_NPA_Revision getRev() {
        return rev;
    }

    public void setRev(NVA_SPR_AUD_NPA_Revision rev) {
        this.rev = rev;
    }

    public Boolean getLoaded() {
        return isLoaded;
    }

    public void setLoaded(Boolean loaded) {
        isLoaded = loaded;
    }

    Boolean isLoaded;

    NVA_SPR_AUD_NPA_Revision rev;
}
