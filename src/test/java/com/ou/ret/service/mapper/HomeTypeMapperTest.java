package com.ou.ret.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HomeTypeMapperTest {

    private HomeTypeMapper homeTypeMapper;

    @BeforeEach
    public void setUp() {
        homeTypeMapper = new HomeTypeMapperImpl();
    }
}
