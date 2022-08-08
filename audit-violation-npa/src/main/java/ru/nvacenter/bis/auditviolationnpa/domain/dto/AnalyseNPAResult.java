package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import java.time.LocalDate;

/**
 * Created by oshesternikova on 01.03.2018.
 */
public class AnalyseNPAResult {
    private Long id;
    private String num;
    private LocalDate date;
    private String text;
    private boolean isDeleted;

    public Long getId() {return id; }
    public void setId(Long id) {this.id = id; }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
