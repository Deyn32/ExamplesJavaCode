package ru.nvacenter.bis.npa.services.dto;

import org.apache.commons.lang3.StringUtils;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_STRUCTURE;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by oshesternikova on 13.02.2018.
 * Базовый класс для публикации документа в представлениях
 */
public abstract class BaseNPAViewService {
    protected final static DateTimeFormatter SHORT_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    protected boolean hasHeader(NVA_SPR_AUD_NPA_STRUCTURE str){
        return !StringUtils.isBlank(str.getElementOriginalType()) ||  !StringUtils.isBlank(str.getElementType());
    }

    protected String createRevHeader(NVA_SPR_AUD_NPA_Revision rev){
        if (rev == null){
            return "Первоначальная";
        }
        return "Ред. от "+ rev.getDtRev().format(SHORT_DATE);
    }

    protected String createDocHeader(NVA_SPR_AUD_NPA doc){
        if (doc == null) return "";
        return String.format("№%s от %s", doc.getNumber(), doc.getDate().format(SHORT_DATE));
    }

    public abstract byte[] create(List<NVA_SPR_AUD_NPA_STRUCTURE> str, NVA_SPR_AUD_NPA_Revision rev, NVA_SPR_AUD_NPA doc);
}
