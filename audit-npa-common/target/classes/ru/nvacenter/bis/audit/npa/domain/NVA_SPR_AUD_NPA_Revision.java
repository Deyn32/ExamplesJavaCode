package ru.nvacenter.bis.audit.npa.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by oshesternikova on 05.04.2017.
 * Ревизия
 */
@Entity()
@Table(name="NVA_SPR_AUD_NPA_Revision")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class NVA_SPR_AUD_NPA_Revision {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /* Документ */
    @Column(name = "ID_NPA")
    private Long id_NPA;

    /* Документ на основе которого редакция*/
    @Column(name = "ID_NPA_Revision")
    private Long id_NPA_Revision;

    /* Дата начала действия */
    @Column(name = "DATE_BEGIN")
    private LocalDate dtBegin;

    /* Дата окончания действия */
    @Column(name = "DATE_END")
    private LocalDate dtEnd;

    /* Дата редакции */
    @Column(name = "DATE_Revision")
    private LocalDate dtRev;

    /* Признак удаления ревизии */
    @Column(name = "Is_Deleted")
    private Boolean isDeleted;

    /* Дата изменения */
    @Column(name = "DATE_Modify")
    private LocalDate dateModify;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_NPA() {
        return id_NPA;
    }

    public void setId_NPA(Long id_NPA) {
        this.id_NPA = id_NPA;
    }

    public LocalDate getDtBegin() {
        return dtBegin;
    }

    public void setDtBegin(LocalDate dtBegin) {
        this.dtBegin = dtBegin;
    }

    public LocalDate getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(LocalDate dtEnd) {
        this.dtEnd = dtEnd;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDate getDtRev() {
        return dtRev;
    }

    public void setDtRev(LocalDate dtRev) {
        this.dtRev = dtRev;
    }

    public LocalDate getDateModify() {
        return dateModify;
    }

    public void setDateModify(LocalDate dateModify) {
        this.dateModify = dateModify;
    }

    public Long getId_NPA_Revision() {
        return id_NPA_Revision;
    }

    public void setId_NPA_Revision(Long id_NPA_Revision) {
        this.id_NPA_Revision = id_NPA_Revision;
    }

}
