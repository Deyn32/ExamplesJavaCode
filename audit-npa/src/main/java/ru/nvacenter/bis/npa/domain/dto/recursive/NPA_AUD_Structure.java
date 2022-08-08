package ru.nvacenter.bis.npa.domain.dto.recursive;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_STRUCTURE;

import java.time.LocalDate;

/**
 * Created by oshesternikova on 25.04.2017.
 * Контракт для передачи данных элемента оглавления
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class NPA_AUD_Structure {
    private  static final Integer maxLen = 100;
    public NPA_AUD_Structure(NVA_SPR_AUD_NPA_STRUCTURE str){
        this.id = str.getId();
        this.dtBegin = str.getDateBegin();
        this.dtEnd = str.getDateEnd();
        this.path = str.getStructurePuth();
        this.type = str.getElementType();
        this.originType = str.getElementOriginalType();
        if (str.getLabel() != null){
            if (str.getLabel().length() > maxLen){
                this.label = str.getLabel().substring(0, maxLen)+"<...>";
            }
            else{
                this.label = str.getLabel();
            }
        }
    }
    private String id;
    private String label;
    private LocalDate dtBegin;
    private LocalDate dtEnd;
    private String path;
    private String type;
    private String originType;

    public NPA_AUD_Structure() {}

    public void setId(String id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDtBegin(LocalDate dtBegin) {
        this.dtBegin = dtBegin;
    }

    public void setDtEnd(LocalDate dtEnd) {
        this.dtEnd = dtEnd;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(String type) { this.type = type; }

    public void setOriginType(String originalType) { this.originType = originalType; }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public LocalDate getDtBegin() {
        return dtBegin;
    }

    public LocalDate getDtEnd() {
        return dtEnd;
    }

    public String getPath() {
        return path;
    }

    public String getType() { return type; }

    public String getOriginType() { return originType; }

}
