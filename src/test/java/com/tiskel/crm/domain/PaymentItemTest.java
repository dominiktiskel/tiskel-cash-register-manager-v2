package com.tiskel.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentItem.class);
        PaymentItem paymentItem1 = new PaymentItem();
        paymentItem1.setId(1L);
        PaymentItem paymentItem2 = new PaymentItem();
        paymentItem2.setId(paymentItem1.getId());
        assertThat(paymentItem1).isEqualTo(paymentItem2);
        paymentItem2.setId(2L);
        assertThat(paymentItem1).isNotEqualTo(paymentItem2);
        paymentItem1.setId(null);
        assertThat(paymentItem1).isNotEqualTo(paymentItem2);
    }
}
