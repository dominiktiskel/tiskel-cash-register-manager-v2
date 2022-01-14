package com.tiskel.crm.service.dto;

import com.tiskel.crm.domain.enumeration.PaymentStatus;
import com.tiskel.crm.domain.enumeration.PaymentType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tiskel.crm.domain.Payment} entity.
 */
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime created;

    @NotNull
    private PaymentStatus status;

    @NotNull
    private PaymentType type;

    @NotNull
    private Boolean isSubscriptionRenewal;

    private String errorMessage;

    private CompanyDTO company;

    private InvoiceDTO invoice;

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

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public Boolean getIsSubscriptionRenewal() {
        return isSubscriptionRenewal;
    }

    public void setIsSubscriptionRenewal(Boolean isSubscriptionRenewal) {
        this.isSubscriptionRenewal = isSubscriptionRenewal;
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

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", isSubscriptionRenewal='" + getIsSubscriptionRenewal() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", company=" + getCompany() +
            ", invoice=" + getInvoice() +
            "}";
    }
}
