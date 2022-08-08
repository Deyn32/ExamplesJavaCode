package ru.nvacenter.bis.audit.npa.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by oshesternikova on 26.01.2017.
 */
@Entity()
@Table(name="NVA_SPR_AUD_Element_Type")
public class NVA_SPR_AUD_Element_Type {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /* Название элемента */
    @Column(name = "Name")
    private String name;

    /* Склонения по падежам */
    @Column(name = "Declensions")
    private String declensions;

    /* Возможные варианты написания */
    @Column(name="Duplicates")
    private String duplicates;

    @Column(name = "Is_Deleted")
    private boolean isDeleted;

    @Column(name = "IsAux")
    private boolean isAux;

    @Column(name = "DATE_BEGIN")
    private LocalDate date_begin;

    @Column(name = "Date_Modify")
    private LocalDate date_modify;

    public LocalDate getDate_begin() {return date_begin; }

    public void setDate_begin(LocalDate begin) {this.date_begin = begin; }

    public LocalDate getDate_modify() {return date_modify; }

    public void setDate_modify(LocalDate modify) { date_modify = modify; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getDeclensions(){
        if (declensions.isEmpty()) return new String[0];
        return declensions.split(";");
    }

    public String[] getDuplicates(){
        if (duplicates.isEmpty()) return new String[0];
        return duplicates.split(";");
    }

    public void setDeclensions(String declensions) { this.declensions = declensions; }

    public void setDuplicates(String duplicates) { this.duplicates = duplicates; }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isAux() {
        return isAux;
    }

    public void setAux(boolean aux) {isAux = aux; }

}
