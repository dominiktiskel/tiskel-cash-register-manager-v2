package com.tiskel.crm.service.dto;

import com.tiskel.crm.domain.enumeration.DeliveryType;
import com.tiskel.crm.domain.enumeration.ReceiptStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tiskel.crm.domain.Receipt} entity.
 */
public class ReceiptDTO implements Serializable {

    private Long id;

    private ReceiptStatus status;

    @NotNull
    private ZonedDateTime created;

    private Integer orderId;

    @NotNull
    private Integer price;

    private Integer tiskelCorporateId;

    private Integer tiskelLicenseId;

    private Integer taxiNumber;

    private Integer driverId;

    private Integer customerId;

    private String customerPhoneNumber;

    private String customerEmail;

    private String emailSentStatus;

    private DeliveryType deliveryType;

    private String printerType;

    private String jsonData;

    private String printData;

    private String jwtData;

    private String errorMessage;

    private CompanyDTO company;

    private CashRegisterDTO cashRegister;

    private TerminalDTO terminal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReceiptStatus getStatus() {
        return status;
    }

    public void setStatus(ReceiptStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTiskelCorporateId() {
        return tiskelCorporateId;
    }

    public void setTiskelCorporateId(Integer tiskelCorporateId) {
        this.tiskelCorporateId = tiskelCorporateId;
    }

    public Integer getTiskelLicenseId() {
        return tiskelLicenseId;
    }

    public void setTiskelLicenseId(Integer tiskelLicenseId) {
        this.tiskelLicenseId = tiskelLicenseId;
    }

    public Integer getTaxiNumber() {
        return taxiNumber;
    }

    public void setTaxiNumber(Integer taxiNumber) {
        this.taxiNumber = taxiNumber;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getEmailSentStatus() {
        return emailSentStatus;
    }

    public void setEmailSentStatus(String emailSentStatus) {
        this.emailSentStatus = emailSentStatus;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getPrinterType() {
        return printerType;
    }

    public void setPrinterType(String printerType) {
        this.printerType = printerType;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getPrintData() {
        return printData;
    }

    public void setPrintData(String printData) {
        this.printData = printData;
    }

    public String getJwtData() {
        return jwtData;
    }

    public void setJwtData(String jwtData) {
        this.jwtData = jwtData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public CashRegisterDTO getCashRegister() {
        return cashRegister;
    }

    public void setCashRegister(CashRegisterDTO cashRegister) {
        this.cashRegister = cashRegister;
    }

    public TerminalDTO getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalDTO terminal) {
        this.terminal = terminal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceiptDTO)) {
            return false;
        }

        ReceiptDTO receiptDTO = (ReceiptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, receiptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceiptDTO{" +
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
            ", company=" + getCompany() +
            ", cashRegister=" + getCashRegister() +
            ", terminal=" + getTerminal() +
            "}";
    }
}
