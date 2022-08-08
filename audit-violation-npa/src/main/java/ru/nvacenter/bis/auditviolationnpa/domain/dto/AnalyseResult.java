package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import java.time.LocalDate;

/**
 * Created by oshesternikova on 01.03.2018.
 */
public class AnalyseResult extends AnalyseResultBase {
    private LocalDate DATE_BEGIN;
    private LocalDate DATE_END;
    /** Результат */
    private String result;
    /** Текст нарушения */
    private String violText;
    /** ID ссылки */
    private Long idLink;


    public LocalDate getDATE_BEGIN() {
        return DATE_BEGIN;
    }

    public void setDATE_BEGIN(LocalDate DATE_BEGIN) {
        this.DATE_BEGIN = DATE_BEGIN;
    }

    public LocalDate getDATE_END() {
        return DATE_END;
    }

    public void setDATE_END(LocalDate DATE_END) {
        this.DATE_END = DATE_END;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getViolText() {
        return violText;
    }

    public void setViolText(String violText) {
        this.violText = violText;
    }

    public Long getIdLink() {
        return idLink;
    }

    public void setIdLink(Long idLink) {
        this.idLink = idLink;
    }
}
