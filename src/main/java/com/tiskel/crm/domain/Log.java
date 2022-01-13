package com.tiskel.crm.domain;

import com.tiskel.crm.domain.enumeration.LogType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Log.
 */
@Entity
@Table(name = "log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LogType type;

    @Column(name = "message")
    private String message;

    @Column(name = "data")
    private String data;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Log id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Log created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public LogType getType() {
        return this.type;
    }

    public Log type(LogType type) {
        this.setType(type);
        return this;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public String getMessage() {
        return this.message;
    }

    public Log message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return this.data;
    }

    public Log data(String data) {
        this.setData(data);
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Log)) {
            return false;
        }
        return id != null && id.equals(((Log) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Log{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", type='" + getType() + "'" +
            ", message='" + getMessage() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
