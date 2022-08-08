package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;

/**
 * Created by oshesternikova on 12.03.2018.
 */
public class AnalyseResultBase {
    private AnalyseNPAResult npa;
    private Long id;
    private NVA_SPR_AUD_NPA_Revision rev;
    private String text;

    public AnalyseNPAResult getNpa() {
        return npa;
    }

    public void setNpa(AnalyseNPAResult npa) {
        this.npa = npa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NVA_SPR_AUD_NPA_Revision getRev() {
        return rev;
    }

    public void setRev(NVA_SPR_AUD_NPA_Revision rev) {
        this.rev = rev;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
