package ru.nvacenter.bis.auditviolationnpa.domain.dto;

/**
 * Created by oshesternikova on 05.03.2018.
 * Модель для случая, когда создание ссылки не получилось
 */
public class FailedResult {
    Long npa;
    Long rev;
    String result;
    String text;
    Long id;

    public FailedResult(Long npa, Long rev, String result) {
        this.npa = npa;
        this.rev = rev;
        this.result = result;
    }

    public Long getNpa() {
        return npa;
    }

    public void setNpa(Long npa) {
        this.npa = npa;
    }

    public Long getRev() {
        return rev;
    }

    public void setRev(Long rev) {
        this.rev = rev;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
