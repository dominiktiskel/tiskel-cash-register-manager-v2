package com.tiskel.crm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentItemMapperTest {

    private PaymentItemMapper paymentItemMapper;

    @BeforeEach
    public void setUp() {
        paymentItemMapper = new PaymentItemMapperImpl();
    }
}
