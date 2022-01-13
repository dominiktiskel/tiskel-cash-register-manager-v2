package com.tiskel.crm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TerminalMapperTest {

    private TerminalMapper terminalMapper;

    @BeforeEach
    public void setUp() {
        terminalMapper = new TerminalMapperImpl();
    }
}
