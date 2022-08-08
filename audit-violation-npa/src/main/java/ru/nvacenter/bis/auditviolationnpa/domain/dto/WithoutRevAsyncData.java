package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by dmihaylov on 04.04.2018.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class WithoutRevAsyncData extends AsyncData {
    private List<WithoutRevision> data;

    public List<WithoutRevision> getData() { return data; }
    public void setData(List<WithoutRevision> data) { this.data = data; }
}
