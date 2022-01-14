package com.tiskel.crm.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tiskel.crm.domain.PaymentItem} entity.
 */
public class PaymentItemDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime created;

    @NotNull
    private Integer daysPayed;

    private TerminalDTO terminal;

    private PaymentDTO payment;

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

    public Integer getDaysPayed() {
        return daysPayed;
    }

    public void setDaysPayed(Integer daysPayed) {
        this.daysPayed = daysPayed;
    }

    public TerminalDTO getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalDTO terminal) {
        this.terminal = terminal;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentItemDTO)) {
            return false;
        }

        PaymentItemDTO paymentItemDTO = (PaymentItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentItemDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", daysPayed=" + getDaysPayed() +
            ", terminal=" + getTerminal() +
            ", payment=" + getPayment() +
            "}";
    }
}
