package ru.nvacenter.bis.npa.domain.dto.doca;

import org.springframework.format.annotation.DateTimeFormat;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_Document_Type;
import ru.nvacenter.bis.npa.domain.dao.Document;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by oshesternikova on 03.04.2017.
 * Контракт для НПА, готовых для копирования
 */
public class NPA_AUD_ForCopy {

    public NPA_AUD_ForCopy(Long id, String num, LocalDate dt, String name, NVA_SPR_AUD_Document_Type el, Boolean hasData, Boolean isEdit) {
        this.num = num;
        this.dt = dt;
        this.name = name;
        this.id = id;
        this.docType = el;
        this.hasData = hasData;
        this.isEditing = isEdit;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public LocalDate getDt() {
        return dt;
    }

    public void setDt(LocalDate dt) {
        this.dt = dt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDataCopy() {
        return docs != null && docs.size() > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Document> getDocs() {
        return docs;
    }

    public void setDocs(List<Document> docs) {
        this.docs = docs;
    }

    public NVA_SPR_AUD_Document_Type getDocType() {
        return docType;
    }

    public void setDocType(NVA_SPR_AUD_Document_Type docType) {
        this.docType = docType;
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    /* Номер */
    private String num;
    /* Дата */
    private LocalDate dt;
    /* Название */
    private String name;
    /* ID */
    private Long id;
    /* Документы */
    private List<Document> docs;
    /* Тип документа */
    private NVA_SPR_AUD_Document_Type docType;
    /* Есть данные в базе */
    private boolean hasData;

    /* Признак изменяющего документа */
    private boolean isEditing;

}
