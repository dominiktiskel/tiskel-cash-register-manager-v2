package com.tiskel.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tiskel.crm.domain.enumeration.PaymentStatus;
import com.tiskel.crm.domain.enumeration.PaymentType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

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
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentType type;

    @NotNull
    @Column(name = "is_subscription_renewal", nullable = false)
    private Boolean isSubscriptionRenewal;

    @Column(name = "error_message")
    private String errorMessage;

    @OneToMany(mappedBy = "payment")
    @JsonIgnoreProperties(value = { "terminal", "payment" }, allowSetters = true)
    private Set<PaymentItem> paymentItems = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cashRegisters", "receipts", "terminals", "payments", "invoices" }, allowSetters = true)
    private Company company;

    @ManyToOne
    @JsonIgnoreProperties(value = { "payments", "company" }, allowSetters = true)
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Payment created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public Payment status(PaymentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentType getType() {
        return this.type;
    }

    public Payment type(PaymentType type) {
        this.setType(type);
        return this;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public Boolean getIsSubscriptionRenewal() {
        return this.isSubscriptionRenewal;
    }

    public Payment isSubscriptionRenewal(Boolean isSubscriptionRenewal) {
        this.setIsSubscriptionRenewal(isSubscriptionRenewal);
        return this;
    }

    public void setIsSubscriptionRenewal(Boolean isSubscriptionRenewal) {
        this.isSubscriptionRenewal = isSubscriptionRenewal;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Payment errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Set<PaymentItem> getPaymentItems() {
        return this.paymentItems;
    }

    public void setPaymentItems(Set<PaymentItem> paymentItems) {
        if (this.paymentItems != null) {
            this.paymentItems.forEach(i -> i.setPayment(null));
        }
        if (paymentItems != null) {
            paymentItems.forEach(i -> i.setPayment(this));
        }
        this.paymentItems = paymentItems;
    }

    public Payment paymentItems(Set<PaymentItem> paymentItems) {
        this.setPaymentItems(paymentItems);
        return this;
    }

    public Payment addPaymentItem(PaymentItem paymentItem) {
        this.paymentItems.add(paymentItem);
        paymentItem.setPayment(this);
        return this;
    }

    public Payment removePaymentItem(PaymentItem paymentItem) {
        this.paymentItems.remove(paymentItem);
        paymentItem.setPayment(null);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Payment company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Payment invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", isSubscriptionRenewal='" + getIsSubscriptionRenewal() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            "}";
    }
}
