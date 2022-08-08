package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oshesternikova on 20.03.2018.
 * Данные по редакциям документа в привязке к нарушениям
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ViolationRevData {
    /*Статус соответствия формулировки нарушения положения НПА*/
    public enum ViolRevState { NOT_KNOWN, YES, NO};
    /*Статус соответствия структурного элемента документа и редакции*/
    public enum StructElemRevState {NONE, NOT_ANALYSED, NOT_FOUND};

    LocalDate dtStart;
    LocalDate dtEnd;
    List<NVA_SPR_AUD_NPA_Revision> revisions;
    ViolRevState violRevstate;
    StructElemRevState structElemRevState;


    public ViolationRevData(LocalDate dtStart, LocalDate dtEnd, ViolRevState violRevstate, StructElemRevState structElemRevState) {
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
        this.violRevstate = violRevstate;
        this.structElemRevState = structElemRevState;
        this.revisions = new ArrayList<>();
    }

    public LocalDate getDtStart() {
        return dtStart;
    }

    public void setDtStart(LocalDate dtStart) {
        this.dtStart = dtStart;
    }

    public LocalDate getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(LocalDate dtEnd) {
        this.dtEnd = dtEnd;
    }

    public List<NVA_SPR_AUD_NPA_Revision> getRevisions() {
        return revisions;
    }

    public void setRevisions(List<NVA_SPR_AUD_NPA_Revision> revisions) {
        this.revisions = revisions;
    }

    public ViolRevState getState() {
        return violRevstate;
    }

    public void setState(ViolRevState state) {
        this.violRevstate = state;
    }

    public StructElemRevState getStructElemRevState() { return structElemRevState;}
    public void setStructElemRevState(StructElemRevState structElemRevState) { this.structElemRevState = structElemRevState;}
}


