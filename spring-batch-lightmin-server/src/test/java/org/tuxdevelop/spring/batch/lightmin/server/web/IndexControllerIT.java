package org.tuxdevelop.spring.batch.lightmin.server.web;

import org.junit.Test;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerIT extends CommonControllerIT {

    @Test
    public void redirectToIndexIT() {
        try {
            this.mockMvc.perform(get("/index")).andExpect(status().is3xxRedirection());
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void initIndexIT() {
        try {
            this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
