package com.ou.ret.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ou.ret.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HomeTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomeTypeDTO.class);
        HomeTypeDTO homeTypeDTO1 = new HomeTypeDTO();
        homeTypeDTO1.setId(1L);
        HomeTypeDTO homeTypeDTO2 = new HomeTypeDTO();
        assertThat(homeTypeDTO1).isNotEqualTo(homeTypeDTO2);
        homeTypeDTO2.setId(homeTypeDTO1.getId());
        assertThat(homeTypeDTO1).isEqualTo(homeTypeDTO2);
        homeTypeDTO2.setId(2L);
        assertThat(homeTypeDTO1).isNotEqualTo(homeTypeDTO2);
        homeTypeDTO1.setId(null);
        assertThat(homeTypeDTO1).isNotEqualTo(homeTypeDTO2);
    }
}
