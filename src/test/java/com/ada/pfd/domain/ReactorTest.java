package com.ada.pfd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ada.pfd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReactorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reactor.class);
        Reactor reactor1 = new Reactor();
        reactor1.setId(1L);
        Reactor reactor2 = new Reactor();
        reactor2.setId(reactor1.getId());
        assertThat(reactor1).isEqualTo(reactor2);
        reactor2.setId(2L);
        assertThat(reactor1).isNotEqualTo(reactor2);
        reactor1.setId(null);
        assertThat(reactor1).isNotEqualTo(reactor2);
    }
}
