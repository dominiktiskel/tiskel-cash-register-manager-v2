package com.tiskel.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TerminalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TerminalDTO.class);
        TerminalDTO terminalDTO1 = new TerminalDTO();
        terminalDTO1.setId(1L);
        TerminalDTO terminalDTO2 = new TerminalDTO();
        assertThat(terminalDTO1).isNotEqualTo(terminalDTO2);
        terminalDTO2.setId(terminalDTO1.getId());
        assertThat(terminalDTO1).isEqualTo(terminalDTO2);
        terminalDTO2.setId(2L);
        assertThat(terminalDTO1).isNotEqualTo(terminalDTO2);
        terminalDTO1.setId(null);
        assertThat(terminalDTO1).isNotEqualTo(terminalDTO2);
    }
}
