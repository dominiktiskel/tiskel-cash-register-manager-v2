package com.tiskel.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
public class Invoice implements Serializable {

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
    @Column(name = "payed_date", nullable = false)
    private ZonedDateTime payedDate;

    @NotNull
    @Column(name = "period_begin_date", nullable = false)
    private ZonedDateTime periodBeginDate;

    @NotNull
    @Column(name = "period_end_date", nullable = false)
    private ZonedDateTime periodEndDate;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull
    @Column(name = "json_data", nullable = false)
    private String jsonData;

    @OneToMany(mappedBy = "invoice")
    @JsonIgnoreProperties(value = { "paymentItems", "company", "invoice" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cashRegisters", "receipts", "terminals", "payments", "invoices" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Invoice created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getPayedDate() {
        return this.payedDate;
    }

    public Invoice payedDate(ZonedDateTime payedDate) {
        this.setPayedDate(payedDate);
        return this;
    }

    public void setPayedDate(ZonedDateTime payedDate) {
        this.payedDate = payedDate;
    }

    public ZonedDateTime getPeriodBeginDate() {
        return this.periodBeginDate;
    }

    public Invoice periodBeginDate(ZonedDateTime periodBeginDate) {
        this.setPeriodBeginDate(periodBeginDate);
        return this;
    }

    public void setPeriodBeginDate(ZonedDateTime periodBeginDate) {
        this.periodBeginDate = periodBeginDate;
    }

    public ZonedDateTime getPeriodEndDate() {
        return this.periodEndDate;
    }

    public Invoice periodEndDate(ZonedDateTime periodEndDate) {
        this.setPeriodEndDate(periodEndDate);
        return this;
    }

    public void setPeriodEndDate(ZonedDateTime periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public String getNumber() {
        return this.number;
    }

    public Invoice number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getJsonData() {
        return this.jsonData;
    }

    public Invoice jsonData(String jsonData) {
        this.setJsonData(jsonData);
        return this;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setInvoice(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setInvoice(this));
        }
        this.payments = payments;
    }

    public Invoice payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Invoice addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setInvoice(this);
        return this;
    }

    public Invoice removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setInvoice(null);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Invoice company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", payedDate='" + getPayedDate() + "'" +
            ", periodBeginDate='" + getPeriodBeginDate() + "'" +
            ", periodEndDate='" + getPeriodEndDate() + "'" +
            ", number='" + getNumber() + "'" +
            ", jsonData='" + getJsonData() + "'" +
            "}";
    }
}
