package ru.nvacenter.bis.npa.domain.dto.recursive;

import ru.nvacenter.bis.audit.npa.domain.NVA_SPR_AUD_NPA_STRUCTURE;

import java.util.List;

/**
 * Created by oshesternikova on 28.03.2017.
 * Контракт для элемента оглавления
 */
public class NPA_AUD_ContentNode {
    public NPA_AUD_ContentNode(NVA_SPR_AUD_NPA_STRUCTURE data, List<NPA_AUD_ContentNode> children) {
        this.data = new NPA_AUD_Structure(data);
        this.children = children;
    }

    public NPA_AUD_Structure getData() {
        return data;
    }

    public List<NPA_AUD_ContentNode> getChildren() {
        return children;
    }

    /* Номер элемента оглавления */
    NPA_AUD_Structure data;
    /* Дочение элементы */
    List<NPA_AUD_ContentNode> children;
}
