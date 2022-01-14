package com.tiskel.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PaymentItem.
 */
@Entity
@Table(name = "payment_item")
public class PaymentItem implements Serializable {

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
    @Column(name = "days_payed", nullable = false)
    private Integer daysPayed;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "receipts", "paymentItems", "company" }, allowSetters = true)
    private Terminal terminal;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentItems", "company", "invoice" }, allowSetters = true)
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public PaymentItem created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Integer getDaysPayed() {
        return this.daysPayed;
    }

    public PaymentItem daysPayed(Integer daysPayed) {
        this.setDaysPayed(daysPayed);
        return this;
    }

    public void setDaysPayed(Integer daysPayed) {
        this.daysPayed = daysPayed;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public PaymentItem terminal(Terminal terminal) {
        this.setTerminal(terminal);
        return this;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PaymentItem payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentItem)) {
            return false;
        }
        return id != null && id.equals(((PaymentItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentItem{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", daysPayed=" + getDaysPayed() +
            "}";
    }
}
