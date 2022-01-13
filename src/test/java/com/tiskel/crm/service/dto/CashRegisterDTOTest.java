package com.tiskel.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CashRegisterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashRegisterDTO.class);
        CashRegisterDTO cashRegisterDTO1 = new CashRegisterDTO();
        cashRegisterDTO1.setId(1L);
        CashRegisterDTO cashRegisterDTO2 = new CashRegisterDTO();
        assertThat(cashRegisterDTO1).isNotEqualTo(cashRegisterDTO2);
        cashRegisterDTO2.setId(cashRegisterDTO1.getId());
        assertThat(cashRegisterDTO1).isEqualTo(cashRegisterDTO2);
        cashRegisterDTO2.setId(2L);
        assertThat(cashRegisterDTO1).isNotEqualTo(cashRegisterDTO2);
        cashRegisterDTO1.setId(null);
        assertThat(cashRegisterDTO1).isNotEqualTo(cashRegisterDTO2);
    }
}
