package org.tuxdevelop.spring.batch.lightmin.server.web;


import org.junit.Test;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class JobConfigurationControllerIT extends CommonControllerIT {

    @Test
    public void getJobConfigurationsIT() {
        try {
            this.mockMvc.perform(get("/jobConfigurations")
                    .param("applicationid", applicationId)
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("jobConfigurations"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void initAddJobConfigurationIT() {
        try {
            this.mockMvc.perform(get("/jobConfigurationAdd")
                    .param("applicationid", applicationId)
            ).andExpect(status().isOk()).andExpect(view().name
                    ("jobConfigurationAdd"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void initEditJobConfigurationIT() {
        try {
            this.mockMvc.perform(get("/jobConfigurationEdit")
                    .param("jobConfigurationId", addedJobConfiguration.getJobConfigurationId().toString())
                    .param("applicationid", applicationId)
            )
                    .andExpect
                            (status().isOk())
                    .andExpect
                            (view().name("jobConfigurationEdit"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void addJobConfigurationIT() {
        try {
            this.mockMvc.perform(post("/jobConfigurationAdd")
                    .param("jobIncrementer", "NONE")
                    .param("schedulerStatus", "INITIALIZED")
                    .param("fixedDelay", "100")
                    .param("initialDelay", "1000")
                    .param("jobName", "simpleJob")
                    .param("jobSchedulerType", "PERIOD")
                    .param("taskExecutorType", "SYNCHRONOUS")
                    .param("applicationId", applicationId)
            )
                    .andExpect
                            (status().isFound())
                    .andExpect
                            (view().name("redirect:jobConfigurations"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateJobConfigurationIT() {
        try {
            this.mockMvc.perform(post("/jobConfigurationEdit")
                    .param("jobConfigurationId", addedJobConfiguration.getJobConfigurationId().toString())
                    .param("jobIncrementer", "NONE")
                    .param("schedulerStatus", "INITIALIZED")
                    .param("fixedDelay", "100")
                    .param("initialDelay", "1000")
                    .param("jobName", "simpleJob")
                    .param("jobSchedulerType", "PERIOD")
                    .param("taskExecutorType", "SYNCHRONOUS")
                    .param("applicationId", applicationId)
            )
                    .andExpect
                            (status().isFound())
                    .andExpect
                            (view().name("redirect:jobConfigurations"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deleteJobConfigurationIT() {
        try {
            this.mockMvc.perform(post("/jobConfigurations")
                    .param("jobConfigurationId", addedJobConfiguration.getJobConfigurationId().toString())
                    .param("applicationid", applicationId)
            )
                    .andExpect
                            (status().isFound())
                    .andExpect
                            (view().name("redirect:jobConfigurations"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void startJobConfigurationSchedulerIT() {
        try {
            this.mockMvc.perform(post("/jobConfigurationSchedulerStart")
                    .param("jobConfigurationId", addedJobConfiguration.getJobConfigurationId().toString())
                    .param("applicationid", applicationId))
                    .andExpect
                            (status().isFound())
                    .andExpect
                            (view().name("redirect:jobConfigurations"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void stopJobConfigurationSchedulerIT() {
        try {
            this.mockMvc.perform(post("/jobConfigurationSchedulerStop")
                    .param("jobConfigurationId", addedJobConfiguration.getJobConfigurationId().toString())
                    .param("applicationid", applicationId))
                    .andExpect
                            (status().isFound())
                    .andExpect
                            (view().name("redirect:jobConfigurations"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
