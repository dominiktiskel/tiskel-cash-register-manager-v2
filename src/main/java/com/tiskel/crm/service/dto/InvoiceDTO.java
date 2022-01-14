package com.tiskel.crm.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tiskel.crm.domain.Invoice} entity.
 */
public class InvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime created;

    @NotNull
    private ZonedDateTime payedDate;

    @NotNull
    private ZonedDateTime periodBeginDate;

    @NotNull
    private ZonedDateTime periodEndDate;

    @NotNull
    private String number;

    @NotNull
    private String jsonData;

    private CompanyDTO company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getPayedDate() {
        return payedDate;
    }

    public void setPayedDate(ZonedDateTime payedDate) {
        this.payedDate = payedDate;
    }

    public ZonedDateTime getPeriodBeginDate() {
        return periodBeginDate;
    }

    public void setPeriodBeginDate(ZonedDateTime periodBeginDate) {
        this.periodBeginDate = periodBeginDate;
    }

    public ZonedDateTime getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(ZonedDateTime periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceDTO)) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", payedDate='" + getPayedDate() + "'" +
            ", periodBeginDate='" + getPeriodBeginDate() + "'" +
            ", periodEndDate='" + getPeriodEndDate() + "'" +
            ", number='" + getNumber() + "'" +
            ", jsonData='" + getJsonData() + "'" +
            ", company=" + getCompany() +
            "}";
    }
}
