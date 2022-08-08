package ru.nvacenter.bis.auditviolationnpa.domain.dao;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by oshesternikova on 21.02.2018.
 * Сущность для уникальной ссылки
 */
@Entity()
@Table(name="NVA_SPR_AUD_Unique_Structure")
public class UniqueStructureElement {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /* Название уникальной ссылки */
    @Column(name = "Name")
    private String name;

    /* Название уникальной ссылки */
    @Column(name = "IdNPA")
    private Long npa;

    @Column(name = "DateModify")
    private Date dateModify;

    @Column(name = "UserModify")
    private String userModify;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="IdUniqueStructure")
    private Set<UniqueRealStructureElement> reals;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNpa() {
        return npa;
    }

    public void setNpa(Long npa) {
        this.npa = npa;
    }

    public Date getDateModify() {
        return dateModify;
    }

    public void setDateModify(Date dateModify) {
        this.dateModify = dateModify;
    }

    public String getUserModify() {
        return userModify;
    }

    public void setUserModify(String userModify) {
        this.userModify = userModify;
    }

    public Set<UniqueRealStructureElement> getReals() {
        return reals;
    }

    public void setReals(Set<UniqueRealStructureElement> reals) {
        this.reals = reals;
    }

    public class NpaTextName{
        private String name;
        private Long npa;

        public NpaTextName(String name, Long npa) {
            this.name = name;
            this.npa = npa;
        }

        @Override
        public boolean equals(Object other){
            if (other == null) return false;
            NpaTextName nt = (NpaTextName)other;
            return StringUtils.equals(nt.name, this.name) && Long.compare(nt.npa, this.npa) == 0;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + name.hashCode();
            result = 31 * result + npa.hashCode();
            return result;
        }
    }

    @Transient
    public NpaTextName getGroupping(){
        return new NpaTextName(this.name, this.npa);
    }

}



