package com.tiskel.crm.service.dto;

import com.tiskel.crm.domain.enumeration.TerminalPayedByType;
import com.tiskel.crm.domain.enumeration.TerminalSubscriptionPeriod;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tiskel.crm.domain.Terminal} entity.
 */
public class TerminalDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime created;

    @NotNull
    private TerminalPayedByType payedBy;

    @NotNull
    private ZonedDateTime payedToDate;

    @NotNull
    private Integer number;

    private String name;

    private String description;

    @NotNull
    private Boolean subscriptionRenewalEnabled;

    @NotNull
    private Integer subscriptionRenewalTrialCount;

    @NotNull
    private TerminalSubscriptionPeriod subscriptionPeriod;

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

    public TerminalPayedByType getPayedBy() {
        return payedBy;
    }

    public void setPayedBy(TerminalPayedByType payedBy) {
        this.payedBy = payedBy;
    }

    public ZonedDateTime getPayedToDate() {
        return payedToDate;
    }

    public void setPayedToDate(ZonedDateTime payedToDate) {
        this.payedToDate = payedToDate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public Boolean getSubscriptionRenewalEnabled() {
        return subscriptionRenewalEnabled;
    }

    public void setSubscriptionRenewalEnabled(Boolean subscriptionRenewalEnabled) {
        this.subscriptionRenewalEnabled = subscriptionRenewalEnabled;
    }

    public Integer getSubscriptionRenewalTrialCount() {
        return subscriptionRenewalTrialCount;
    }

    public void setSubscriptionRenewalTrialCount(Integer subscriptionRenewalTrialCount) {
        this.subscriptionRenewalTrialCount = subscriptionRenewalTrialCount;
    }

    public TerminalSubscriptionPeriod getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public void setSubscriptionPeriod(TerminalSubscriptionPeriod subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
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
        if (!(o instanceof TerminalDTO)) {
            return false;
        }

        TerminalDTO terminalDTO = (TerminalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, terminalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TerminalDTO{" +
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
            ", company=" + getCompany() +
            "}";
    }
}
