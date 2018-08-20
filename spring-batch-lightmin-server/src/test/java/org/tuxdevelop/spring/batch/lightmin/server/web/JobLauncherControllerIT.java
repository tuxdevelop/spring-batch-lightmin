package org.tuxdevelop.spring.batch.lightmin.server.web;

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
            this.mockMvc.perform(get("/jobLaunchers")
                    .param("applicationid", this.applicationId)
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("jobLaunchers"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void launchJobIT() {
        try {
            this.mockMvc.perform(post("/jobLauncher")
                    .param("jobName", "simpleJob")
                    .param("id", this.applicationId)
            ).andExpect(status().isFound());
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void configureJobLauncherTest() {
        try {
            this.mockMvc.perform(get("/jobLauncher")
                    .param("jobName", "simpleJob")
                    .param("id", this.applicationId)
            ).andExpect(status().isOk()).andExpect(view().name("jobLauncher"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }
}
