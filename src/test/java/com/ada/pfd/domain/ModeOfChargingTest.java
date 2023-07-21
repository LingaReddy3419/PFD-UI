package com.ada.pfd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ada.pfd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModeOfChargingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModeOfCharging.class);
        ModeOfCharging modeOfCharging1 = new ModeOfCharging();
        modeOfCharging1.setId(1L);
        ModeOfCharging modeOfCharging2 = new ModeOfCharging();
        modeOfCharging2.setId(modeOfCharging1.getId());
        assertThat(modeOfCharging1).isEqualTo(modeOfCharging2);
        modeOfCharging2.setId(2L);
        assertThat(modeOfCharging1).isNotEqualTo(modeOfCharging2);
        modeOfCharging1.setId(null);
        assertThat(modeOfCharging1).isNotEqualTo(modeOfCharging2);
    }
}
