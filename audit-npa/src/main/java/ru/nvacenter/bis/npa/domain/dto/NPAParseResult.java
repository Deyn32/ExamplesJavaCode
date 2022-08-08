package ru.nvacenter.bis.npa.domain.dto;

import ru.nvacenter.bis.audit.npa.domain.dto.NPANode;

import java.util.List;

/**
 * Created by oshesternikova on 30.05.2017.
 */
public class NPAParseResult {
    NPANode tree;
    List<NPATypeNumberMap> uncertainties;

    public NPANode getTree() {
        return tree;
    }

    public void setTree(NPANode tree) {
        this.tree = tree;
    }

    public List<NPATypeNumberMap> getUncertainties() {
        return uncertainties;
    }

    public void setUncertainties(List<NPATypeNumberMap> uncertainties) {
        this.uncertainties = uncertainties;
    }


}
