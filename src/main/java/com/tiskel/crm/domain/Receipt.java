package com.tiskel.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tiskel.crm.domain.enumeration.DeliveryType;
import com.tiskel.crm.domain.enumeration.ReceiptStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Receipt.
 */
@Entity
@Table(name = "receipt")
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReceiptStatus status;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @Column(name = "order_id")
    private Integer orderId;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "tiskel_corporate_id")
    private Integer tiskelCorporateId;

    @Column(name = "tiskel_license_id")
    private Integer tiskelLicenseId;

    @Column(name = "taxi_number")
    private Integer taxiNumber;

    @Column(name = "driver_id")
    private Integer driverId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_phone_number")
    private String customerPhoneNumber;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "email_sent_status")
    private String emailSentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DeliveryType deliveryType;

    @Column(name = "printer_type")
    private String printerType;

    @Column(name = "json_data")
    private String jsonData;

    @Column(name = "print_data")
    private String printData;

    @Column(name = "jwt_data")
    private String jwtData;

    @Column(name = "error_message")
    private String errorMessage;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cashRegisters", "receipts", "terminals", "payments", "invoices" }, allowSetters = true)
    private Company company;

    @ManyToOne
    @JsonIgnoreProperties(value = { "receipts", "company" }, allowSetters = true)
    private CashRegister cashRegister;

    @ManyToOne
    @JsonIgnoreProperties(value = { "receipts", "paymentItems", "company" }, allowSetters = true)
    private Terminal terminal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Receipt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReceiptStatus getStatus() {
        return this.status;
    }

    public Receipt status(ReceiptStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReceiptStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Receipt created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public Receipt orderId(Integer orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getPrice() {
        return this.price;
    }

    public Receipt price(Integer price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTiskelCorporateId() {
        return this.tiskelCorporateId;
    }

    public Receipt tiskelCorporateId(Integer tiskelCorporateId) {
        this.setTiskelCorporateId(tiskelCorporateId);
        return this;
    }

    public void setTiskelCorporateId(Integer tiskelCorporateId) {
        this.tiskelCorporateId = tiskelCorporateId;
    }

    public Integer getTiskelLicenseId() {
        return this.tiskelLicenseId;
    }

    public Receipt tiskelLicenseId(Integer tiskelLicenseId) {
        this.setTiskelLicenseId(tiskelLicenseId);
        return this;
    }

    public void setTiskelLicenseId(Integer tiskelLicenseId) {
        this.tiskelLicenseId = tiskelLicenseId;
    }

    public Integer getTaxiNumber() {
        return this.taxiNumber;
    }

    public Receipt taxiNumber(Integer taxiNumber) {
        this.setTaxiNumber(taxiNumber);
        return this;
    }

    public void setTaxiNumber(Integer taxiNumber) {
        this.taxiNumber = taxiNumber;
    }

    public Integer getDriverId() {
        return this.driverId;
    }

    public Receipt driverId(Integer driverId) {
        this.setDriverId(driverId);
        return this;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getCustomerId() {
        return this.customerId;
    }

    public Receipt customerId(Integer customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerPhoneNumber() {
        return this.customerPhoneNumber;
    }

    public Receipt customerPhoneNumber(String customerPhoneNumber) {
        this.setCustomerPhoneNumber(customerPhoneNumber);
        return this;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public Receipt customerEmail(String customerEmail) {
        this.setCustomerEmail(customerEmail);
        return this;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getEmailSentStatus() {
        return this.emailSentStatus;
    }

    public Receipt emailSentStatus(String emailSentStatus) {
        this.setEmailSentStatus(emailSentStatus);
        return this;
    }

    public void setEmailSentStatus(String emailSentStatus) {
        this.emailSentStatus = emailSentStatus;
    }

    public DeliveryType getDeliveryType() {
        return this.deliveryType;
    }

    public Receipt deliveryType(DeliveryType deliveryType) {
        this.setDeliveryType(deliveryType);
        return this;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getPrinterType() {
        return this.printerType;
    }

    public Receipt printerType(String printerType) {
        this.setPrinterType(printerType);
        return this;
    }

    public void setPrinterType(String printerType) {
        this.printerType = printerType;
    }

    public String getJsonData() {
        return this.jsonData;
    }

    public Receipt jsonData(String jsonData) {
        this.setJsonData(jsonData);
        return this;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getPrintData() {
        return this.printData;
    }

    public Receipt printData(String printData) {
        this.setPrintData(printData);
        return this;
    }

    public void setPrintData(String printData) {
        this.printData = printData;
    }

    public String getJwtData() {
        return this.jwtData;
    }

    public Receipt jwtData(String jwtData) {
        this.setJwtData(jwtData);
        return this;
    }

    public void setJwtData(String jwtData) {
        this.jwtData = jwtData;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Receipt errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Receipt company(Company company) {
        this.setCompany(company);
        return this;
    }

    public CashRegister getCashRegister() {
        return this.cashRegister;
    }

    public void setCashRegister(CashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }

    public Receipt cashRegister(CashRegister cashRegister) {
        this.setCashRegister(cashRegister);
        return this;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Receipt terminal(Terminal terminal) {
        this.setTerminal(terminal);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Receipt)) {
            return false;
        }
        return id != null && id.equals(((Receipt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Receipt{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            ", orderId=" + getOrderId() +
            ", price=" + getPrice() +
            ", tiskelCorporateId=" + getTiskelCorporateId() +
            ", tiskelLicenseId=" + getTiskelLicenseId() +
            ", taxiNumber=" + getTaxiNumber() +
            ", driverId=" + getDriverId() +
            ", customerId=" + getCustomerId() +
            ", customerPhoneNumber='" + getCustomerPhoneNumber() + "'" +
            ", customerEmail='" + getCustomerEmail() + "'" +
            ", emailSentStatus='" + getEmailSentStatus() + "'" +
            ", deliveryType='" + getDeliveryType() + "'" +
            ", printerType='" + getPrinterType() + "'" +
            ", jsonData='" + getJsonData() + "'" +
            ", printData='" + getPrintData() + "'" +
            ", jwtData='" + getJwtData() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            "}";
    }
}
