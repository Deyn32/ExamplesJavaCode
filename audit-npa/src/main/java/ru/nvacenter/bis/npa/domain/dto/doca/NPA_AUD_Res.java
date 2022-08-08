package ru.nvacenter.bis.npa.domain.dto.doca;

import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA;
import ru.nvacenter.bis.npa.domain.dao.Document;

import java.util.List;

/**
 * Created by oshesternikova on 14.03.2017.
 * Контракт для возвращения данных при копировании из ДОКИ в ФОИС
 */
public class NPA_AUD_Res {
    public NPA_AUD_Res(List<NVA_SPR_AUD_NPA> $docs, List<String> $errors, List<Document> $docadocs){
        this.docs = $docs;
        this.errors = $errors;
        this.docadocs = $docadocs;
    }
    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    /* Ошибки */
    List<String> errors;

    public List<NVA_SPR_AUD_NPA> getDocs() {
        return docs;
    }

    public void setDocs(List<NVA_SPR_AUD_NPA> docs) {
        this.docs = docs;
    }

    /* Доступные документы */
    List<NVA_SPR_AUD_NPA> docs;

    public List<Document> getDocadocs() {
        return docadocs;
    }

    public void setDocadocs(List<Document> docadocs) {
        this.docadocs = docadocs;
    }

    /* Доступные документы из ДОКи*/
    List<Document> docadocs;

}
