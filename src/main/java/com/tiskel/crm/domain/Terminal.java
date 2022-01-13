package com.tiskel.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tiskel.crm.domain.enumeration.TerminalPayedByType;
import com.tiskel.crm.domain.enumeration.TerminalSubscriptionPeriod;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Terminal.
 */
@Entity
@Table(name = "terminal")
public class Terminal implements Serializable {

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
    @Column(name = "payed_by", nullable = false)
    private TerminalPayedByType payedBy;

    @NotNull
    @Column(name = "payed_to_date", nullable = false)
    private ZonedDateTime payedToDate;

    @NotNull
    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "subscription_renewal_enabled", nullable = false)
    private Boolean subscriptionRenewalEnabled;

    @NotNull
    @Column(name = "subscription_renewal_trial_count", nullable = false)
    private Integer subscriptionRenewalTrialCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_period", nullable = false)
    private TerminalSubscriptionPeriod subscriptionPeriod;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "error_message")
    private String errorMessage;

    @OneToMany(mappedBy = "terminal")
    @JsonIgnoreProperties(value = { "company", "cashRegister", "terminal" }, allowSetters = true)
    private Set<Receipt> receipts = new HashSet<>();

    @OneToMany(mappedBy = "terminal")
    @JsonIgnoreProperties(value = { "terminal", "payment" }, allowSetters = true)
    private Set<PaymentItem> paymentItems = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cashRegisters", "receipts", "terminals", "payments", "invoices" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Terminal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Terminal created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public TerminalPayedByType getPayedBy() {
        return this.payedBy;
    }

    public Terminal payedBy(TerminalPayedByType payedBy) {
        this.setPayedBy(payedBy);
        return this;
    }

    public void setPayedBy(TerminalPayedByType payedBy) {
        this.payedBy = payedBy;
    }

    public ZonedDateTime getPayedToDate() {
        return this.payedToDate;
    }

    public Terminal payedToDate(ZonedDateTime payedToDate) {
        this.setPayedToDate(payedToDate);
        return this;
    }

    public void setPayedToDate(ZonedDateTime payedToDate) {
        this.payedToDate = payedToDate;
    }

    public Integer getNumber() {
        return this.number;
    }

    public Terminal number(Integer number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public Terminal name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Terminal description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSubscriptionRenewalEnabled() {
        return this.subscriptionRenewalEnabled;
    }

    public Terminal subscriptionRenewalEnabled(Boolean subscriptionRenewalEnabled) {
        this.setSubscriptionRenewalEnabled(subscriptionRenewalEnabled);
        return this;
    }

    public void setSubscriptionRenewalEnabled(Boolean subscriptionRenewalEnabled) {
        this.subscriptionRenewalEnabled = subscriptionRenewalEnabled;
    }

    public Integer getSubscriptionRenewalTrialCount() {
        return this.subscriptionRenewalTrialCount;
    }

    public Terminal subscriptionRenewalTrialCount(Integer subscriptionRenewalTrialCount) {
        this.setSubscriptionRenewalTrialCount(subscriptionRenewalTrialCount);
        return this;
    }

    public void setSubscriptionRenewalTrialCount(Integer subscriptionRenewalTrialCount) {
        this.subscriptionRenewalTrialCount = subscriptionRenewalTrialCount;
    }

    public TerminalSubscriptionPeriod getSubscriptionPeriod() {
        return this.subscriptionPeriod;
    }

    public Terminal subscriptionPeriod(TerminalSubscriptionPeriod subscriptionPeriod) {
        this.setSubscriptionPeriod(subscriptionPeriod);
        return this;
    }

    public void setSubscriptionPeriod(TerminalSubscriptionPeriod subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Terminal active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Terminal errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Set<Receipt> getReceipts() {
        return this.receipts;
    }

    public void setReceipts(Set<Receipt> receipts) {
        if (this.receipts != null) {
            this.receipts.forEach(i -> i.setTerminal(null));
        }
        if (receipts != null) {
            receipts.forEach(i -> i.setTerminal(this));
        }
        this.receipts = receipts;
    }

    public Terminal receipts(Set<Receipt> receipts) {
        this.setReceipts(receipts);
        return this;
    }

    public Terminal addReceipt(Receipt receipt) {
        this.receipts.add(receipt);
        receipt.setTerminal(this);
        return this;
    }

    public Terminal removeReceipt(Receipt receipt) {
        this.receipts.remove(receipt);
        receipt.setTerminal(null);
        return this;
    }

    public Set<PaymentItem> getPaymentItems() {
        return this.paymentItems;
    }

    public void setPaymentItems(Set<PaymentItem> paymentItems) {
        if (this.paymentItems != null) {
            this.paymentItems.forEach(i -> i.setTerminal(null));
        }
        if (paymentItems != null) {
            paymentItems.forEach(i -> i.setTerminal(this));
        }
        this.paymentItems = paymentItems;
    }

    public Terminal paymentItems(Set<PaymentItem> paymentItems) {
        this.setPaymentItems(paymentItems);
        return this;
    }

    public Terminal addPaymentItem(PaymentItem paymentItem) {
        this.paymentItems.add(paymentItem);
        paymentItem.setTerminal(this);
        return this;
    }

    public Terminal removePaymentItem(PaymentItem paymentItem) {
        this.paymentItems.remove(paymentItem);
        paymentItem.setTerminal(null);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Terminal company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Terminal)) {
            return false;
        }
        return id != null && id.equals(((Terminal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Terminal{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", payedBy='" + getPayedBy() + "'" +
            ", payedToDate='" + getPayedToDate() + "'" +
            ", number=" + getNumber() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", subscriptionRenewalEnabled='" + getSubscriptionRenewalEnabled() + "'" +
            ", subscriptionRenewalTrialCount=" + getSubscriptionRenewalTrialCount() +
            ", subscriptionPeriod='" + getSubscriptionPeriod() + "'" +
            ", active='" + getActive() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            "}";
    }
}
