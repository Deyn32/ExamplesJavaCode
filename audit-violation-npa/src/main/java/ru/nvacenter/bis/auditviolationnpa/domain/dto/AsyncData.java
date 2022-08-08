package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import java.util.Date;
import java.util.UUID;

/**
 * Created by dmihaylov on 04.04.2018.
 * Общий класс для асинхронных запросов
 */
public abstract class AsyncData {

    private UUID id;
    private String status;
    private String error;
    private boolean isCompleted;
    private boolean isCanceled;
    private Date startTime;
    private Date endTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean getIsCanceled() { return isCanceled; }

    public void setIsCanceled(boolean flag) { isCanceled = flag; }
}
