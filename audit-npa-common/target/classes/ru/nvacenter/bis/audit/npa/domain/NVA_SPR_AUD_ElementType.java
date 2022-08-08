package ru.nvacenter.bis.audit.npa.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * Created by alaptev on 03.02.2017.
 */
@Entity
public class NVA_SPR_AUD_ElementType {
    @Id
    private String id;
    private String name;
    private boolean deleted;
    private LocalDate started;
    private LocalDate ended;

    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public boolean getDeleted() { return this.deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public LocalDate getStarted() { return this.started; }
    public void setStarted(LocalDate started) { this.started = started; }

    public LocalDate getEnded() { return this.ended; }
    public void setEnded(LocalDate ended) { this.ended = ended; }
}
