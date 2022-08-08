package ru.nvacenter.bis.audit.npa.domain;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by rgolovin on 18.09.2015.
 */
@Entity()
@Table(name="NVA_SPR_AUD_NPA")
public class NVA_SPR_AUD_NPA {
        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @Column(name = "ID")
        private Long id;

        /**
         * Название документа
         */
        @Column(name = "Name_Full")
        private String name;

        /**
         * Номер документа
         */
        @Column(name = "Num")
        private String number;

        /**
         * Дата документа
         */
        @Column(name = "DATE_NPA")
        private LocalDate date;

        /**
         * Родитель
         */
        @Column(name = "id_Parent")
        private Long idParent;

        /**
         * Тип документа
         */
        @ManyToOne
        @NotFound(action = NotFoundAction.IGNORE)
        @JoinColumn(name = "ID_Document_Type", nullable=true)
        private NVA_SPR_AUD_Document_Type type;

        @Column(name = "ID_Out_NPA")
        private String prevId;

        @Column(name = "Is_Deleted")
        private Boolean isDeleted ;

        @Column(name = "IsEditing")
        private Boolean isEditing ;

        /** дата окончания действия документа */
        @Column(name = "DATE_END")
        private LocalDate dateEnd;

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

        public String getNumber() {
                return number;
        }

        public void setNumber(String number) {
                this.number = number;
        }

        public LocalDate getDate() {
                return date;
        }

        public void setDate(LocalDate date) {
                this.date = date;
        }

        public NVA_SPR_AUD_Document_Type getType() {
                return type;
        }

        public void setType(NVA_SPR_AUD_Document_Type type) {
                this.type = type;
        }

        public Long getIdParent() {
                return idParent;
        }

        public void setIdParent(Long idParent) {
                this.idParent = idParent;
        }

        public String getPrevId() {
                return prevId;
        }

        public void setPrevId(String prevId) {
                this.prevId = prevId;
        }

        public Boolean getDeleted() {
                return isDeleted;
        }

        public void setDeleted(Boolean deleted) {
                isDeleted = deleted;
        }

        public Boolean getEditing() {
                return isEditing;
        }

        public void setEditing(Boolean editing) {
                isEditing = editing;
        }

        public LocalDate getDateEnd() {
                return dateEnd;
        }

        public void setDateEnd(LocalDate dateEnd) {
                this.dateEnd = dateEnd;
        }
}
