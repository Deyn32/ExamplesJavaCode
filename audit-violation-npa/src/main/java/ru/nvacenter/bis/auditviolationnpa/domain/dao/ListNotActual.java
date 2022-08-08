package ru.nvacenter.bis.auditviolationnpa.domain.dao;

import com.google.gwt.aria.client.SearchRole;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by oshesternikova on 25.04.2018.
 */
@Entity()
@Table(name="NVA_SPR_AUD_LinksNotActual")
public class ListNotActual {
    @EmbeddedId
    ListNotActualPK listNotActualPK;
    @Column(name = "UserModify")
    String userModify;
    @Column(name = "DateModify")
    LocalDate dateModify;

    public ListNotActualPK getListNotActualPK() {
        return listNotActualPK;
    }

    public void setListNotActualPK(ListNotActualPK listNotActualPK) {
        this.listNotActualPK = listNotActualPK;
    }

    public String getUserModify() {
        return userModify;
    }

    public void setUserModify(String userModify) {
        this.userModify = userModify;
    }

    public LocalDate getDateModify() {
        return dateModify;
    }

    public void setDateModify(LocalDate dateModify) {
        this.dateModify = dateModify;
    }
}
