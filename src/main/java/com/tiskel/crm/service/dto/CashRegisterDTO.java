package com.tiskel.crm.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tiskel.crm.domain.CashRegister} entity.
 */
public class CashRegisterDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime created;

    private ZonedDateTime lastUpdate;

    private String name;

    private String description;

    private String elzabId;

    private String elzabLicense;

    @NotNull
    private Boolean active;

    private String errorMessage;

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

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getElzabId() {
        return elzabId;
    }

    public void setElzabId(String elzabId) {
        this.elzabId = elzabId;
    }

    public String getElzabLicense() {
        return elzabLicense;
    }

    public void setElzabLicense(String elzabLicense) {
        this.elzabLicense = elzabLicense;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CashRegisterDTO)) {
            return false;
        }

        CashRegisterDTO cashRegisterDTO = (CashRegisterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cashRegisterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CashRegisterDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", elzabId='" + getElzabId() + "'" +
            ", elzabLicense='" + getElzabLicense() + "'" +
            ", active='" + getActive() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", company=" + getCompany() +
            "}";
    }
}
