package ru.nvacenter.bis.auditviolationnpa.models;

import ru.nvacenter.bis.auditviolationnpa.domain.dto.ViolationRevData;
import java.util.List;

/**
 * Created by dmihaylov on 27.04.2018.
 */
public class SaveState {
    private Long idNpaLink;
    private List<Long> idRevs;
    private ViolationRevData.ViolRevState oldState;
    private ViolationRevData.ViolRevState newState;

    public Long getIdNpaLink() { return idNpaLink; }
    public void setIdNpaLink(Long id) {this.idNpaLink = id;}

    public List<Long> getIdRevs() { return idRevs; }
    public void setIdRevs(List<Long> idRevs) { this.idRevs = idRevs; }

    public ViolationRevData.ViolRevState getOldState() { return oldState; }
    public void setOldState(ViolationRevData.ViolRevState oldState) {this.oldState = oldState; }

    public ViolationRevData.ViolRevState getNewState() { return newState; }
    public void setNewState(ViolationRevData.ViolRevState newState) { this.newState = newState; }
}
