package ru.nvacenter.bis.npa.domain.dto;

import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;

/**
 * Created by dmihaylov on 04.06.2018.
 */
public class StateDownloadNpa extends NVA_SPR_AUD_NPA {

    public StateDownloadNpa() {}

    public StateDownloadNpa(NVA_SPR_AUD_NPA npa){
        this.setNumber(npa.getNumber());
        this.setDate(npa.getDate());
        this.setType(npa.getType());
        this.setId(npa.getId());
        this.setDateEnd(npa.getDateEnd());
        this.setDeleted(npa.getDeleted());
        this.setEditing(npa.getEditing());
        this.setIdParent(npa.getIdParent());
        this.setPrevId(npa.getPrevId());
        this.setName(npa.getName());
    }

    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
