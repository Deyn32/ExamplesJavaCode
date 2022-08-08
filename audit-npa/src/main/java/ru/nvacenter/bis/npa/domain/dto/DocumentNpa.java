package ru.nvacenter.bis.npa.domain.dto;

import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_Organization_REF_NPA;

import java.util.List;

/**
 * Created by dmihaylov on 22.05.2018.
 */
public class DocumentNpa extends NVA_SPR_AUD_NPA {
    private List<RevisionNpa> revisions;
    private List<NVA_SPR_AUD_Organization_REF_NPA> orgsNpa;

    public List<RevisionNpa> getRevisions() {return revisions;}
    public void setRevisions(List<RevisionNpa> revisions) {this.revisions = revisions;}

    public List<NVA_SPR_AUD_Organization_REF_NPA> getOrgsNpa() {return orgsNpa;}
    public void setOrgsNpa(List<NVA_SPR_AUD_Organization_REF_NPA> orgsNpa) {this.orgsNpa = orgsNpa;}
}
