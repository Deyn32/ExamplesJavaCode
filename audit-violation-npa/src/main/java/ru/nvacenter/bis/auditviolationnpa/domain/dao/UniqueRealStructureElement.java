package ru.nvacenter.bis.auditviolationnpa.domain.dao;

import org.hibernate.annotations.Generated;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by oshesternikova on 21.02.2018.
 */
@Entity()
@Table(name="NVA_SPR_AUD_Unique_Real_Structure")
public class UniqueRealStructureElement {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUniqueStructure() {
        return idUniqueStructure;
    }

    public void setIdUniqueStructure(Long idUniqueStructure) {
        this.idUniqueStructure = idUniqueStructure;
    }

    public String getIdRealStructure() {
        return idRealStructure;
    }

    public void setIdRealStructure(String idRealStructure) {
        this.idRealStructure = idRealStructure;
    }

    public Long getIdRev() {
        return idRev;
    }

    public void setIdRev(Long idRev) {
        this.idRev = idRev;
    }

    public Date getDateModify() {
        return dateModify;
    }

    public void setDateModify(Date dateModify) {
        this.dateModify = dateModify;
    }

    public String getUserModify() {
        return userModify;
    }

    public void setUserModify(String userModify) {
        this.userModify = userModify;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdUniqueStructure")
    private Long idUniqueStructure;

    @Column(name = "IdRealStructure")
    private String idRealStructure;

    @Column(name = "IdRev")
    private Long idRev;

    @Column(name = "DateModify")
    private Date dateModify;

    @Column(name = "UserModify")
    private String userModify;

    @Column(name = "IsDeleted")
    private boolean isDeleted;

}
