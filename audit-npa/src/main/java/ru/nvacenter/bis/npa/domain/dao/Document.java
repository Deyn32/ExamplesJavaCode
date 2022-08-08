package ru.nvacenter.bis.npa.domain.dao;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oshesternikova on 19.01.2017.
 * Документ в БД "ДОКА"
 */

@Entity()
@Table(name="Document")
public class Document {

    private final static Locale ru_RU = new Locale("ru", "RU");
    /* Форматы даты, хранящейся в БД ДОКа */
    public final  static  DateTimeFormatter[] DateLawFormats = {
            DateTimeFormatter.ofPattern("d MMMM uuuu", ru_RU),
            DateTimeFormatter.ofPattern("d MMM uuuu", ru_RU)
    };

    /* Форматы русифицированные */
    private final  static List<DateTimeFormatter> DateLawRuFormats = new ArrayList<>();

    /* Паттерн для извлечения даты с полным месяцем */
    private final static Pattern FullDatePattern;

    /* Паттерн для извлечения даты с сокращенным месяцем */
    private final static Pattern ShortDatePattern;
    static {
        Calendar mCalendar = Calendar.getInstance();
        Map<String, Integer> monthes = mCalendar.getDisplayNames(Calendar.MONTH, Calendar.LONG, ru_RU);
        Map<String, Integer> monthes2 = mCalendar.getDisplayNames(Calendar.MONTH, Calendar.SHORT, ru_RU);

        String str = "^\\d\\d?\\s*("+String.join("|",monthes.keySet())+")\\s*\\d\\d\\d\\d";
        FullDatePattern = Pattern.compile(str);


        String str2 = "^\\d\\d?\\s*("+String.join("|",monthes2.keySet())+").?\\s*\\d\\d\\d\\d";
        ShortDatePattern = Pattern.compile(str2);

        DateLawRuFormats.add(DateTimeFormatter.ofLocalizedDate(
                FormatStyle.MEDIUM).withLocale(ru_RU));

        DateLawRuFormats.add(DateTimeFormatter.ofLocalizedDate(
                FormatStyle.LONG).withLocale(ru_RU));

        DateLawRuFormats.add(DateTimeFormatter.ofLocalizedDate(
                FormatStyle.SHORT).withLocale(ru_RU));
    }

    @Id
    @GeneratedValue(generator = "uniqueGeneratorName4Criterion")
    @GenericGenerator(name = "uniqueGeneratorName4Criterion", strategy = "guid")
    @Column(name = "documentId")
    private String id;
    /* Номер ФЗ */
    @Column(name = "numberLaw")
    private String numberLaw;
    /* Дата ФЗ */
    @Column(name = "dateLaw")
    private String dateLaw;

    /* Название */
    @Column(name = "lawName")
    private String lawName;

    @Column(name = "caption")
    private String caption;

    @Column(name = "commentary")
    private String commentary;

    @Column(name="deleted")
    private LocalDate delSign;

    @Column(name="revisionDate")
    private LocalDate revisionDate;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNumberLaw() {
        return numberLaw;
    }
    public void setNumberLaw(String nl) {
        this.numberLaw = nl;
    }

    public String getDateLaw() {
        return dateLaw;
    }

    public void setDateLaw(String dateLaw) {
        this.dateLaw = dateLaw;
    }

    public String getLawName() {
        return lawName;
    }

    public void setLawName(String lawName) {
        this.lawName = lawName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    @Transient
    private boolean isParsed = false;
    @Transient
    private LocalDate dateAsLocalDate = null;
    /* Дата документа как дата */
    public LocalDate getDateLawAsDate(){
        if(isParsed) return  dateAsLocalDate;
        isParsed = true;

        if (this.dateLaw == null) return  dateAsLocalDate;
        Matcher matcher = FullDatePattern.matcher(this.dateLaw);
        if (matcher.find()){
            String s2 = ToNormilze(matcher.group());
            dateAsLocalDate = LocalDate.parse(s2, DateLawFormats[0]);
            if (dateAsLocalDate != null) return dateAsLocalDate;
        }

        matcher = ShortDatePattern.matcher(this.dateLaw);
        if (matcher.matches()){
            String s2 = ToNormilze(matcher.group());
            dateAsLocalDate = LocalDate.parse(s2, DateLawFormats[1]);
            if (dateAsLocalDate != null) return dateAsLocalDate;
        }

        for(DateTimeFormatter dtf : DateLawRuFormats){
            try{
                dateAsLocalDate = LocalDate.parse(this.dateLaw, dtf);
                if (dateAsLocalDate != null) return dateAsLocalDate;
            }
            catch (Exception ex2) {}
        }

        try{
            dateAsLocalDate = LocalDate.parse(this.dateLaw);
        }
        catch (Exception ex3) {}

        return  dateAsLocalDate;
    }

    private static String ToNormilze(String dt){
        String[] arr = dt.split("\\s+");
        return String.join(" ", arr);
    }


    public LocalDate getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(LocalDate revisionDate) {
        this.revisionDate = revisionDate;
    }
}
