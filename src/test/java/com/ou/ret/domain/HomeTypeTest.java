package com.ou.ret.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ou.ret.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HomeTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomeType.class);
        HomeType homeType1 = new HomeType();
        homeType1.setId(1L);
        HomeType homeType2 = new HomeType();
        homeType2.setId(homeType1.getId());
        assertThat(homeType1).isEqualTo(homeType2);
        homeType2.setId(2L);
        assertThat(homeType1).isNotEqualTo(homeType2);
        homeType1.setId(null);
        assertThat(homeType1).isNotEqualTo(homeType2);
    }
}
