package ru.nvacenter.bis.npa.domain.dto.revisions;

import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;

/**
 * Created by oshesternikova on 05.04.2017.
 */
public class NPA_AUD_Rev_Data {

    public NPA_AUD_Rev_Data(NVA_SPR_AUD_NPA doc) {
        this.doc = doc;
        this.revs = new NVA_SPR_AUD_NPA_Revision[0];
    }

    public NPA_AUD_Rev_Data(NVA_SPR_AUD_NPA doc, NVA_SPR_AUD_NPA_Revision[] revs) {
        this.doc = doc;
        this.revs = revs;
    }

    public NVA_SPR_AUD_NPA getDoc() {
        return doc;
    }

    public NVA_SPR_AUD_NPA_Revision[] getRevs() {
        return revs;
    }

    NVA_SPR_AUD_NPA_Revision[] revs;

    NVA_SPR_AUD_NPA doc;
}
