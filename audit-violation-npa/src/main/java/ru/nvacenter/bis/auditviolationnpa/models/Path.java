package ru.nvacenter.bis.auditviolationnpa.models;

import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by oshesternikova on 22.02.2018.
 * Путь в дереве
 */
public class Path extends ArrayList<PathElement> {
    private boolean isFailed;
    private NVA_SPR_AUD_NPA_STRUCTURE str;
    @Override
    public String toString() {
        String res = String.join(",", this.stream().map(m -> m.toString()).collect(Collectors.toList()));
        return res;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }
}
