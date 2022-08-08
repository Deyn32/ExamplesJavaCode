package ru.nvacenter.bis.audit.npa.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by rgolovin on 18.09.2015.
 */
@Entity()
@Table(name="[NVA_SPR_AUD_NPA_STRUCTURE]")
public class NVA_SPR_AUD_NPA_STRUCTURE {
        /**
         * ID
         */
        @Id
        @GeneratedValue(generator = "uniqueGeneratorName4Criterion")
        @GenericGenerator(name = "uniqueGeneratorName4Criterion", strategy = "guid")
        @Column(name = "documentElementId")
        private String id;

        /**
         * Родитель
         */
        @Column(name = "fk_parentElementId")
        private String fk_parentElementId;


        /**
         * Заголовок документа
         */
        @Column(name = "Id_SprAudNPA")
        private Long id_SprAudNPA;

        public Long getId_SprAudNPAStr() {
                return id_SprAudNPAStr;
        }

        public void setId_SprAudNPAStr(Long id_SprAudNPAStr) {
                this.id_SprAudNPAStr = id_SprAudNPAStr;
        }

        /**
         * ссылка на ревизию
         */
        @Column(name = "Id_SprAudNPAStr")
        private Long id_SprAudNPAStr;

        /**
         * Порядковый номер
         */
        @Column(name = "elementOrder")
        private Long elementOrder;

        /**
         * Тип структурной единицы
         */
        @Column(name = "elementType")
        private String elementType;

        /**
         * Исходный тип структурной единицы (полученный при загрузке)
         */
        @Column(name = "elementOriginalType")
        private String elementOriginalType;

        @Column(name = "elementNumber")
        private String elementNumber;

        @Column(name = "text")
        private String text;

        @Column(name = "deleted")
        private LocalDate deleted;

        @Column(name = "updated")
        private LocalDate updated;

        @Column(name = "version")
        private Integer version;

        @Column(name = "elementLevel")
        private Integer elementLevel;

        @Column(name = "DATE_BEGIN")
        private LocalDate dateBegin;

        @Column(name = "DATE_END")
        private LocalDate dateEnd;
        /****************************/

        /**
         * Уровень вложенности
         */
        @Transient
        private Integer lv;

        /**
         * Полный путь структуры
         */
        @Transient
        private String structPuth;


        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public Long getElementOrder() {
                return elementOrder;
        }

        public void setElementOrder(Long elementOrder) {
                this.elementOrder = elementOrder;
        }

        public String getElementType() {
                return elementType;
        }

        public void setElementType(String elementType) {
                this.elementType = elementType;
        }

        public String getElementNumber() {
                return elementNumber;
        }

        public void setElementNumber(String elementNumber) {
                this.elementNumber = elementNumber;
        }

        public String getText() {
                return text;
        }

        public void setText(String text) {
                this.text = text;
        }

        public String getFk_parentElementId() {
                return fk_parentElementId;
        }

        public void setFk_parentElementId(String fk_parentElementId) {
                this.fk_parentElementId = fk_parentElementId;
        }

        public Long getId_SprAudNPA() {
                return id_SprAudNPA;
        }

        public void setId_SprAudNPA(Long id_SprAudNPA) {
                this.id_SprAudNPA = id_SprAudNPA;
        }

        public Integer getLevel() {
                return lv;
        }

        public void setLevel(Integer level) {
                this.lv = level;
        }

        public String getStructurePuth() { return structPuth == null ? "" : structPuth; }

        public void setStructurePuth(String structurePuth) {
                this.structPuth = structurePuth;
        }

        public String getLabel() {
                String s1 = formattedPath(true);
                String s2 = text== null ? "" : text;
                return s1.concat(s2);
        }

        public LocalDate getUpdated() {
                return updated;
        }

        public void setUpdated(LocalDate updated) {
                this.updated = updated;
        }

        public Integer getVersion() {
                return version;
        }

        public void setVersion(Integer version) {
                this.version = version;
        }

        public LocalDate getDeleted() {
                return deleted;
        }

        public void setDeleted(LocalDate deleted) {
                this.deleted = deleted;
        }


        public Integer getElementLevel() {
                return elementLevel;
        }

        public void setElementLevel(Integer elementLevel) {
                this.elementLevel = elementLevel;
        }


        public LocalDate getDateBegin() {
                return dateBegin;
        }

        public void setDateBegin(LocalDate dateBegin) {
                this.dateBegin = dateBegin;
        }

        public LocalDate getDateEnd() {
                return dateEnd;
        }

        public void setDateEnd(LocalDate dateEnd) {
                this.dateEnd = dateEnd;
        }

        public String getElementOriginalType() {
                return elementOriginalType;
        }

        public void setElementOriginalType(String elementOriginalType) {
                this.elementOriginalType = elementOriginalType;
        }

    /**
     * Форматированный заголовок
     * @param origPrefer Предпочитаем данные из оргинального загруженного документа (true) или введенные уже нами данные (false)
     * @return
     */
    public String formattedPath(boolean origPrefer) {
            String s0 = null;
            if (origPrefer)
                s0 = elementOriginalType == null ? (elementType == null ? "" : elementType) : elementOriginalType;
            else s0 = elementType == null ? (elementOriginalType == null ? "" : elementOriginalType) : elementType;
            String s1 = elementNumber == null?"":elementNumber;
            String con = s0.concat(" ").concat(s1);
            if (!s0.isEmpty() && !s1.isEmpty()) {
                    char ch = s1.charAt(s1.length() - 1);
                    if (Character.isLetterOrDigit(ch))
                            return con.concat(". ");
                }
                return con.trim();
        }

}
