package ru.nvacenter.bis.npa.domain.dto.recursive;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oshesternikova on 30.05.2017.
 * Модель для узла НПА (иерархическая структура)
 */
public class NPANode {
    public NPANode() {
        this.data = new NPANodeData();
        this.children = new ArrayList<>();
    }

    /*Дочерние элементы*/
    private List<NPANode> children;
    /*Элемент оглавления*/
    private NPANodeData data;
    private NPANode parent;
    /* Номер узла */
    private Long order;

    public void setOrder (Long order) {this.order = order;}
    public Long getOrder() {return this.order;}

    public List<NPANode> getChildren() { return this.children;}

    public NPANodeData getData() { return data; }

    @JsonIgnore
    public NPANode getParent() {
        return parent;
    }

    public void setParent(NPANode parent) {
        this.parent = parent;
    }

    public void addChild(NPANode ch){
        this.children.add(ch);
    }

    @JsonIgnore
    public NPANode getLastNode(){
        if (this.children.size() == 0) return null;
        return this.children.get(this.children.size() - 1);
    }
}

