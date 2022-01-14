package com.tiskel.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @Column(name = "nip")
    private String nip;

    @Column(name = "regon")
    private String regon;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "post_code")
    private String postCode;

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "receipts", "company" }, allowSetters = true)
    private Set<CashRegister> cashRegisters = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "cashRegister", "terminal" }, allowSetters = true)
    private Set<Receipt> receipts = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "receipts", "paymentItems", "company" }, allowSetters = true)
    private Set<Terminal> terminals = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "paymentItems", "company", "invoice" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "payments", "company" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Company created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getNip() {
        return this.nip;
    }

    public Company nip(String nip) {
        this.setNip(nip);
        return this;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getRegon() {
        return this.regon;
    }

    public Company regon(String regon) {
        this.setRegon(regon);
        return this;
    }

    public void setRegon(String regon) {
        this.regon = regon;
    }

    public String getStreet() {
        return this.street;
    }

    public Company street(String street) {
        this.setStreet(street);
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public Company city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public Company postCode(String postCode) {
        this.setPostCode(postCode);
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Set<CashRegister> getCashRegisters() {
        return this.cashRegisters;
    }

    public void setCashRegisters(Set<CashRegister> cashRegisters) {
        if (this.cashRegisters != null) {
            this.cashRegisters.forEach(i -> i.setCompany(null));
        }
        if (cashRegisters != null) {
            cashRegisters.forEach(i -> i.setCompany(this));
        }
        this.cashRegisters = cashRegisters;
    }

    public Company cashRegisters(Set<CashRegister> cashRegisters) {
        this.setCashRegisters(cashRegisters);
        return this;
    }

    public Company addCashRegister(CashRegister cashRegister) {
        this.cashRegisters.add(cashRegister);
        cashRegister.setCompany(this);
        return this;
    }

    public Company removeCashRegister(CashRegister cashRegister) {
        this.cashRegisters.remove(cashRegister);
        cashRegister.setCompany(null);
        return this;
    }

    public Set<Receipt> getReceipts() {
        return this.receipts;
    }

    public void setReceipts(Set<Receipt> receipts) {
        if (this.receipts != null) {
            this.receipts.forEach(i -> i.setCompany(null));
        }
        if (receipts != null) {
            receipts.forEach(i -> i.setCompany(this));
        }
        this.receipts = receipts;
    }

    public Company receipts(Set<Receipt> receipts) {
        this.setReceipts(receipts);
        return this;
    }

    public Company addReceipt(Receipt receipt) {
        this.receipts.add(receipt);
        receipt.setCompany(this);
        return this;
    }

    public Company removeReceipt(Receipt receipt) {
        this.receipts.remove(receipt);
        receipt.setCompany(null);
        return this;
    }

    public Set<Terminal> getTerminals() {
        return this.terminals;
    }

    public void setTerminals(Set<Terminal> terminals) {
        if (this.terminals != null) {
            this.terminals.forEach(i -> i.setCompany(null));
        }
        if (terminals != null) {
            terminals.forEach(i -> i.setCompany(this));
        }
        this.terminals = terminals;
    }

    public Company terminals(Set<Terminal> terminals) {
        this.setTerminals(terminals);
        return this;
    }

    public Company addTerminal(Terminal terminal) {
        this.terminals.add(terminal);
        terminal.setCompany(this);
        return this;
    }

    public Company removeTerminal(Terminal terminal) {
        this.terminals.remove(terminal);
        terminal.setCompany(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setCompany(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setCompany(this));
        }
        this.payments = payments;
    }

    public Company payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Company addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setCompany(this);
        return this;
    }

    public Company removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setCompany(null);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setCompany(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setCompany(this));
        }
        this.invoices = invoices;
    }

    public Company invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Company addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setCompany(this);
        return this;
    }

    public Company removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", nip='" + getNip() + "'" +
            ", regon='" + getRegon() + "'" +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", postCode='" + getPostCode() + "'" +
            "}";
    }
}
