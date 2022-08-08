package ru.nvacenter.bis.auditviolationnpa.domain.dto;

/**
 * Created by dmihaylov on 04.04.2018.
 */
public class WithoutRevision extends UnloadedNPA {

    private Long idRevision;

    public Long getIdRevision() { return idRevision; }
    public void setIdRevision(Long idRevs) { this.idRevision = idRevs; }

}
