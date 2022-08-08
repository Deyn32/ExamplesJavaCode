package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by dmihaylov on 18.07.2018.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class RevLinksAsyncData extends AsyncData {
    private List<ViolationRevData> data;

    public List<ViolationRevData> getData() {
        return data;
    }

    public void setData(List<ViolationRevData> data) {
        this.data = data;
    }
}
