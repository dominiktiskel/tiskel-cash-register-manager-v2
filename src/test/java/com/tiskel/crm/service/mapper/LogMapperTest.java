package com.tiskel.crm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogMapperTest {

    private LogMapper logMapper;

    @BeforeEach
    public void setUp() {
        logMapper = new LogMapperImpl();
    }
}
