package com.ada.pfd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ada.pfd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operations.class);
        Operations operations1 = new Operations();
        operations1.setId(1L);
        Operations operations2 = new Operations();
        operations2.setId(operations1.getId());
        assertThat(operations1).isEqualTo(operations2);
        operations2.setId(2L);
        assertThat(operations1).isNotEqualTo(operations2);
        operations1.setId(null);
        assertThat(operations1).isNotEqualTo(operations2);
    }
}
