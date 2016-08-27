package org.tuxdevelop.spring.batch.lightmin.server.web;

import org.junit.Test;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AdminControllerIT extends CommonControllerIT {

    @Test
    public void initAdminIT() {
        try {
            this.mockMvc.perform(get("/admin/")).andExpect(status().isOk()).andExpect(view().name("admin"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
