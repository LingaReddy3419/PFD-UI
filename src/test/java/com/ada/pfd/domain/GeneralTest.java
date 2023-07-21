package com.ada.pfd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ada.pfd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeneralTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(General.class);
        General general1 = new General();
        general1.setId(1L);
        General general2 = new General();
        general2.setId(general1.getId());
        assertThat(general1).isEqualTo(general2);
        general2.setId(2L);
        assertThat(general1).isNotEqualTo(general2);
        general1.setId(null);
        assertThat(general1).isNotEqualTo(general2);
    }
}
