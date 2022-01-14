package com.tiskel.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CashRegister.
 */
@Entity
@Table(name = "cash_register")
public class CashRegister implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @Column(name = "last_update")
    private ZonedDateTime lastUpdate;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "elzab_id")
    private String elzabId;

    @Column(name = "elzab_license")
    private String elzabLicense;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "error_message")
    private String errorMessage;

    @OneToMany(mappedBy = "cashRegister")
    @JsonIgnoreProperties(value = { "company", "cashRegister", "terminal" }, allowSetters = true)
    private Set<Receipt> receipts = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cashRegisters", "receipts", "terminals", "payments", "invoices" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CashRegister id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public CashRegister created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    public CashRegister lastUpdate(ZonedDateTime lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getName() {
        return this.name;
    }

    public CashRegister name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public CashRegister description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getElzabId() {
        return this.elzabId;
    }

    public CashRegister elzabId(String elzabId) {
        this.setElzabId(elzabId);
        return this;
    }

    public void setElzabId(String elzabId) {
        this.elzabId = elzabId;
    }

    public String getElzabLicense() {
        return this.elzabLicense;
    }

    public CashRegister elzabLicense(String elzabLicense) {
        this.setElzabLicense(elzabLicense);
        return this;
    }

    public void setElzabLicense(String elzabLicense) {
        this.elzabLicense = elzabLicense;
    }

    public Boolean getActive() {
        return this.active;
    }

    public CashRegister active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public CashRegister errorMessage(String errorMessage) {
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
            this.receipts.forEach(i -> i.setCashRegister(null));
        }
        if (receipts != null) {
            receipts.forEach(i -> i.setCashRegister(this));
        }
        this.receipts = receipts;
    }

    public CashRegister receipts(Set<Receipt> receipts) {
        this.setReceipts(receipts);
        return this;
    }

    public CashRegister addReceipt(Receipt receipt) {
        this.receipts.add(receipt);
        receipt.setCashRegister(this);
        return this;
    }

    public CashRegister removeReceipt(Receipt receipt) {
        this.receipts.remove(receipt);
        receipt.setCashRegister(null);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CashRegister company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CashRegister)) {
            return false;
        }
        return id != null && id.equals(((CashRegister) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CashRegister{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", elzabId='" + getElzabId() + "'" +
            ", elzabLicense='" + getElzabLicense() + "'" +
            ", active='" + getActive() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            "}";
    }
}
