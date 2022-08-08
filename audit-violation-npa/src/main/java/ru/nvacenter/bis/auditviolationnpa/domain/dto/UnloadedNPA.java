/**
 * Created by dmihaylov on 02.04.2018.
 */
package ru.nvacenter.bis.auditviolationnpa.domain.dto;

import java.time.LocalDate;


public class UnloadedNPA {
    private Long id;
    private Long idNpa;
    private String num;
    private String caption;
    private String text;
    private LocalDate date;
    private String violationText;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdNpa() { return  idNpa; }
    public void setIdNpa(Long idNpa) { this.idNpa = idNpa; }

    public String getNum() { return num; }
    public void setNum(String num) { this.num = num; }

    public String getCaption() { return caption; }
    public void setCaption(String cap) { caption = cap; }

    public String getText() {return text;}
    public void setText(String text) { this.text = text; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) {this.date = date; }

    public String getViolationText() { return violationText; }
    public void setViolationText(String violText) { violationText = violText; }
}
