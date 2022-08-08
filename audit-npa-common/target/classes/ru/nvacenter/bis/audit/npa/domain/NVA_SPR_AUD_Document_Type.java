package ru.nvacenter.bis.audit.npa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by rgolovin on 18.09.2015.
 */
@Entity()
@Table(name="NVA_SPR_AUD_Document_Type")
public class NVA_SPR_AUD_Document_Type {
        @Id
        @Column(name = "ID")
        private Long id;

        /* Название документа */
        @Column(name = "Name_Nominative")
        private String name;

        /* Возможные дубликаты в названии */
        @Column(name = "Name_Duplicates")
        private String duplicates;

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

        public String getDuplicates() {
                return duplicates;
        }

        public void setDuplicates(String duplicates) {
                this.duplicates = duplicates;
        }

}
