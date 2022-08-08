package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import ru.nvacenter.bis.audit.npa.domain.dao.NVA_SPR_AUD_NPA_Revision;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPA;
import ru.nvacenter.bis.auditviolationcommon.server.domain.NVA_SPR_FKAU_ViolationNPAStructure;
import java.time.format.DateTimeFormatter;

/**
 * Created by oshesternikova on 19.03.2018.
 */
public class ViolationLinkData {
    private AnalyseNPAResult npa;
    private String text;
    private Long id;
    private String error;
    private boolean status;

    public ViolationLinkData() {}

    public ViolationLinkData(String text, NVA_SPR_FKAU_ViolationNPA lnpa, NVA_SPR_FKAU_ViolationNPAStructure str, NVA_SPR_AUD_NPA_Revision r, boolean  status, boolean isNpaDel) {
        this.text = text;
        this.status = status;
        AnalyseNPAResult anr = new AnalyseNPAResult();
        anr.setId(lnpa.getIdSource());
        anr.setNum(lnpa.getNumber());
        anr.setText(lnpa.getCaption());
        anr.setDate(lnpa.getDate());
        anr.setDeleted(isNpaDel);
        this.npa = anr;
        if (str != null) {
            this.id = str.getId();
            if (str.getIdSource() == null)
                error = "В БД нет ссылки на элемент";
            else{
                if (r != null)
                    error = "Ссылка изначально проставлена для ред. от "+r.getDtRev().format(DateTimeFormatter.ofPattern("dd.MM.yy"));
                else
                    error = "Не найдена редакция положения";
            }
        }
       else{
            //error = "Не загружен документ";
        }
    }

    public AnalyseNPAResult getNpa() {
        return npa;
    }

    public void setNpa(AnalyseNPAResult npa) {
        this.npa = npa;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
