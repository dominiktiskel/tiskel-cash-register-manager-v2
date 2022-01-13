package com.tiskel.crm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CashRegisterMapperTest {

    private CashRegisterMapper cashRegisterMapper;

    @BeforeEach
    public void setUp() {
        cashRegisterMapper = new CashRegisterMapperImpl();
    }
}
