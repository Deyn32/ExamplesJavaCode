package ru.nvacenter.bis.npa.domain.dao;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by oshesternikova on 23.01.2017.
 */

@Entity()
@Table(name="DocumentStructure")
@FilterDefs(value = {
        @FilterDef(name = "DS_NotDeleted", defaultCondition = "deleted is NULL")
})
public class DocumentStructure {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getElemOrder() {
        return elemOrder;
    }

    public void setElemOrder(Integer elemOrder) {
        this.elemOrder = elemOrder;
    }

    public String getElemType() {
        return elemType;
    }

    public void setElemType(String elemType) {
        this.elemType = elemType;
    }

    public String getElemNumber() {
        return elemNumber;
    }

    public void setElemNumber(String elemNumber) {
        this.elemNumber = elemNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Integer getElemLevel() {
        return elemLevel;
    }

    public void setElemLevel(Integer elemLevel) {
        this.elemLevel = elemLevel;
    }

    public String getFkParentElemId() {
        return fkParentElemId;
    }

    public void setFkParentElemId(String fkParentElemId) {
        this.fkParentElemId = fkParentElemId;
    }

    public LocalDate getDeleted() {
        return deleted;
    }

    public void setDeleted(LocalDate deleted) {
        this.deleted = deleted;
    }

    public String getFkDocumentId() {
        return fkDocumentId;
    }

    public void setFkDocumentId(String fkDocumentId) {
        this.fkDocumentId = fkDocumentId;
    }

    @Id
    @GeneratedValue(generator = "uniqueGeneratorName4Criterion")
    @GenericGenerator(name = "uniqueGeneratorName4Criterion", strategy = "guid")
    @Column(name = "documentElementId")
    private String id;

    @Column(name = "elementOrder")
    private Integer elemOrder;

    @Column(name = "elementType")
    private String elemType;

    @Column(name = "elementNumber")
    private String elemNumber;

    @Column(name = "text")
    private String text;

    @Column(name = "elementLevel")
    private Integer elemLevel;

    @Column(name = "fk_parentElementId")
    private String fkParentElemId;

    @Column(name = "deleted")
    private LocalDate deleted;

    @Column(name = "fk_documentId")
    private String fkDocumentId;

}
