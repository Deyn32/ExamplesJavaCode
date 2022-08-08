package ru.nvacenter.bis.npa.domain.dto.recursive;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by dmihaylov on 09.06.2017.
 */
public class NPANodeData {
    public NPANodeData() {
        type = null;
        num = null;
        name = null;
    }

    private String id;

    /* Тип узла */
    private String type;
    private String originalType;
    /* Порядковый номер */
    private String num;
    /* Содержимое */
    private String name;
    /*Родитель*/
    private String fk_parentElementId;

    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }

    public String getOriginalType() { return originalType; }

    public  void setOriginalType(String originalType) { this.originalType = originalType; }

    @JsonIgnore
    public String getFormattedType() {
        if(type == null || type.contains("_"))
            return "";
        return type;
    }

    @JsonIgnore
    public  String getFormattedNum() {
        if(num == null)
            return "";
        return num;
    }

    @JsonIgnore
    public  String getFormattedName() {
        if(name == null)
            return StringUtils.EMPTY;
        return name;
    }

    public String getFk_parentElementId() { return fk_parentElementId; }

    public void setFk_parentElementId(String fk_parent) {this.fk_parentElementId = fk_parent;}

    public String getId() {return id;}

    public void setId(String idDoc) {this.id = idDoc;}

    public String getNum() { return num;}

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
