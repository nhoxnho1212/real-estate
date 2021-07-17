package com.ou.ret.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtraMapperTest {

    private ExtraMapper extraMapper;

    @BeforeEach
    public void setUp() {
        extraMapper = new ExtraMapperImpl();
    }
}
