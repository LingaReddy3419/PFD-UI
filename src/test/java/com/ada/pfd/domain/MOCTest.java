package com.ada.pfd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ada.pfd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MOCTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MOC.class);
        MOC mOC1 = new MOC();
        mOC1.setId(1L);
        MOC mOC2 = new MOC();
        mOC2.setId(mOC1.getId());
        assertThat(mOC1).isEqualTo(mOC2);
        mOC2.setId(2L);
        assertThat(mOC1).isNotEqualTo(mOC2);
        mOC1.setId(null);
        assertThat(mOC1).isNotEqualTo(mOC2);
    }
}
