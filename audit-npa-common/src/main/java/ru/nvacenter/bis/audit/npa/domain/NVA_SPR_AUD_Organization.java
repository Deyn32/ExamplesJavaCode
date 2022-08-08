package ru.nvacenter.bis.audit.npa.domain;

import javax.persistence.*;

/**
 * Created by oshesternikova on 07.03.2018.
 * Организация, утвердившая НПА
 */
@Entity()
@Table(name="NVA_SPR_AUD_Organization")
public class NVA_SPR_AUD_Organization {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "Name_Full")
    private String fullName;
    @Column(name = "Name_Short")
    private String shortName;
    @Column(name = "IsDeleted")
    private boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
