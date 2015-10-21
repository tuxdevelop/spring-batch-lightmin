package org.tuxdevelop.spring.batch.lightmin.controller;

import org.junit.Test;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class JobLauncherControllerIT extends CommonControllerIT {

    @Test
    public void initJobLaunchersIT() {
        try {
            this.mockMvc.perform(get("/jobLaunchers")).andExpect(status().isOk()).andExpect(view().name("jobLaunchers"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void launchJobIT() {
        try {
            this.mockMvc.perform(post("/jobLaunchers")
                            .param("jobName", "simpleJob")
            ).andExpect(status().isFound()).andExpect(view().name
                    ("redirect:jobLaunchers"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }
}
