package ru.nvacenter.bis.npa.domain.dto;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import ru.nvacenter.bis.npa.domain.dto.recursive.NPANode;

import java.util.List;

/**
 * Created by oshesternikova on 25.09.2017.
 * Элемент дерева после сопоставления двух узлов
 */
public class NPARevisionCompareNode {

    public NPANode getNodeLeft() {
        return nodeLeft;
    }

    public NPANode getNodeRight() {
        return nodeRight;
    }

    public List<NPARevisionCompareNode> getChildren() {
        return children;
    }

    public  void setChildren(List<NPARevisionCompareNode> list) {
        children = list;
    }

    public List<DiffMatchPatch.Diff> getDiffs() {
        return diffs;
    }

    private NPANode nodeLeft;
    private NPANode nodeRight;
    private List<NPARevisionCompareNode> children;
    private List<DiffMatchPatch.Diff> diffs;

    public NPARevisionCompareNode(NPANode nodeLeft, NPANode nodeRight, List<NPARevisionCompareNode> children, List<DiffMatchPatch.Diff> diffs) {
        this.nodeLeft = nodeLeft;
        this.nodeRight = nodeRight;
        this.children = children;
        this.diffs = diffs;
    }

    public boolean hasAnyChanges(){
        boolean b = nodeLeft == null || nodeRight == null || (diffs != null && diffs.size() > 0);
        if (b) return b;
        for (NPARevisionCompareNode ch:
             children) {
            boolean b2 = ch.hasAnyChanges();
            if (b2) return b2;
        }
        return false;
    }
}
