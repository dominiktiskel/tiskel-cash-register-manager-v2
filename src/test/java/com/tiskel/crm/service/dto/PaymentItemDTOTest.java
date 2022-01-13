package com.tiskel.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentItemDTO.class);
        PaymentItemDTO paymentItemDTO1 = new PaymentItemDTO();
        paymentItemDTO1.setId(1L);
        PaymentItemDTO paymentItemDTO2 = new PaymentItemDTO();
        assertThat(paymentItemDTO1).isNotEqualTo(paymentItemDTO2);
        paymentItemDTO2.setId(paymentItemDTO1.getId());
        assertThat(paymentItemDTO1).isEqualTo(paymentItemDTO2);
        paymentItemDTO2.setId(2L);
        assertThat(paymentItemDTO1).isNotEqualTo(paymentItemDTO2);
        paymentItemDTO1.setId(null);
        assertThat(paymentItemDTO1).isNotEqualTo(paymentItemDTO2);
    }
}
