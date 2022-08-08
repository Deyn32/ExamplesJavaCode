package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by oshesternikova on 01.03.2018.
 */
public class StructureHierarchy {
    private String id;
    private String parent_id;
    private LocalDate dateStart;
    private LocalDate dateEnd;

    public StructureHierarchy(String id, String parent_id, LocalDate dateStart, LocalDate dateEnd) {
        this.id = id;
        this.parent_id = parent_id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }
}
