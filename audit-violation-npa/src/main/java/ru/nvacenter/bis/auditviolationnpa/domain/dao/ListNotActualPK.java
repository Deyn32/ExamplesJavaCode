package ru.nvacenter.bis.auditviolationnpa.domain.dao;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by oshesternikova on 25.04.2018.
 */
@Embeddable
public class ListNotActualPK implements Serializable {
    @Column(name = "IdViolationNpa")
    Long idViolationNpa;
    @Column(name = "IdRev")
    Long idRev;
    @Column(name = "IdSource")
    String idSource;

    public ListNotActualPK(){
    }

    public ListNotActualPK(Long idViolationNpa, Long idRev, String idSource) {
        this.idViolationNpa = idViolationNpa;
        this.idRev = idRev;
        this.idSource = idSource;
    }

    public Long getIdViolationNpa() {
        return idViolationNpa;
    }

    public void setIdViolationNpa(Long idViolationNpa) {
        this.idViolationNpa = idViolationNpa;
    }

    public Long getIdRev() {
        return idRev;
    }

    public void setIdRev(Long idRev) {
        this.idRev = idRev;
    }

    public String getIdSource() {
        return idSource;
    }

    public void setIdSource(String idSource) {
        this.idSource = idSource;
    }
}
