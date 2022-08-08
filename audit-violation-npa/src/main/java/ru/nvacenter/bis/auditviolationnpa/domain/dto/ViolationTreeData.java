package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import ru.nvacenter.bis.auditviolationcommon.server.domain.ViolationData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oshesternikova on 19.03.2018.
 * Данные по нарушениям в виде дерева
 */
public class ViolationTreeData {
    private Long id;
    private String text;
    private List<ViolationLinkData> links;
    private LocalDate dtBegin;
    private LocalDate dtEnd;

    public ViolationTreeData() {}

    public ViolationTreeData(ViolationData vd) {
        this.id = vd.getId();
        this.text = vd.getViolationText();
        this.links = new ArrayList<>();
        this.dtBegin = vd.getDateBegin();
        this.dtEnd = vd.getDateEnd();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ViolationLinkData> getLinks() {
        return links;
    }

    public LocalDate getDtBegin() { return  dtBegin; }
    public void setDtBegin(LocalDate dtBegin) { this.dtBegin = dtBegin; }

    public LocalDate getDtEnd() { return dtEnd; }
    public void setDtEnd(LocalDate dtEnd) { this.dtEnd = dtEnd; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id;}

}
