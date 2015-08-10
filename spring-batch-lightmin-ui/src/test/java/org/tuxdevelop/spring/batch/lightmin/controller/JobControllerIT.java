package org.tuxdevelop.spring.batch.lightmin.controller;


import org.junit.Test;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class JobControllerIT extends CommonControllerIT {

    @Test
    public void initJobsIT() {
        try {
            this.mockMvc.perform(get("/jobs")).andExpect(status().isOk()).andExpect(view().name("jobs"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
