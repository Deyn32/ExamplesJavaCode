package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by oshesternikova on 14.03.2018.
 * Объект для передачи данных в асинхроном запросе
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class AnalyseAsyncData extends AsyncData {
    private List<AnalyseResult> data;

    public List<AnalyseResult> getData() {
        return data;
    }

    public void setData(List<AnalyseResult> data) {
        this.data = data;
    }

}
