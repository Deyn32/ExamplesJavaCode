package ru.nvacenter.bis.npa.domain.dto;

import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;

import java.util.List;

/**
 * Created by dmihaylov on 22.05.2018.
 */
public class RevisionNpa extends NVA_SPR_AUD_NPA_Revision {
    private List<NVA_SPR_AUD_NPA_STRUCTURE> listStruct;

    public List<NVA_SPR_AUD_NPA_STRUCTURE> getListStruct(){return listStruct;}
    public void setListStruct(List<NVA_SPR_AUD_NPA_STRUCTURE> list) {this.listStruct = list;}
}
