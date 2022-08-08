package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPA;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by dmihaylov on 17.04.2018.
 */
public class Violation {
    private Long id;
    private LocalDate dtBegin;
    private LocalDate dtEnd;
    private List<NVA_SPR_FKAU_ViolationNPA> listLinksNpa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDtBegin() { return dtBegin; }
    public void setDtBegin(LocalDate dtBegin) { this.dtBegin = dtBegin; }

    public LocalDate getDtEnd() { return dtEnd; }
    public void setDtEnd(LocalDate dtEnd) { this.dtEnd = dtEnd; }

    public List<NVA_SPR_FKAU_ViolationNPA> getListLinksNpa() { return listLinksNpa; }
    public void setListLinksNpa(List<NVA_SPR_FKAU_ViolationNPA> listLinksNpa) { this.listLinksNpa = listLinksNpa; }
}
