package com.ada.pfd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ada.pfd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImpellerTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImpellerType.class);
        ImpellerType impellerType1 = new ImpellerType();
        impellerType1.setId(1L);
        ImpellerType impellerType2 = new ImpellerType();
        impellerType2.setId(impellerType1.getId());
        assertThat(impellerType1).isEqualTo(impellerType2);
        impellerType2.setId(2L);
        assertThat(impellerType1).isNotEqualTo(impellerType2);
        impellerType1.setId(null);
        assertThat(impellerType1).isNotEqualTo(impellerType2);
    }
}
