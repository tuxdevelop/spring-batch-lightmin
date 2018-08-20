package org.tuxdevelop.spring.batch.lightmin.server.web;


import org.junit.Ignore;
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
            this.mockMvc.perform(get("/jobSchedulerConfigurations")
                    .param("applicationid", this.applicationId)
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("jobSchedulerConfigurations"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getJobListenerConfigurationssIT() {
        try {
            this.mockMvc.perform(get("/jobListenerConfigurations")
                    .param("applicationid", this.applicationId)
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("jobListenerConfigurations"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void initAddJobConfigurationIT() {
        try {
            this.mockMvc.perform(get("/jobSchedulerConfigurationAdd")
                    .param("applicationid", this.applicationId)
            ).andExpect(status().isOk()).andExpect(view().name
                    ("jobSchedulerConfigurationAdd"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void initAddJobListenerConfigurationIT() {
        try {
            this.mockMvc.perform(get("/jobListenerConfigurationAdd")
                    .param("applicationid", this.applicationId)
            ).andExpect(status().isOk()).andExpect(view().name
                    ("jobListenerConfigurationAdd"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void initEditJobSchedulerConfigurationIT() {
        try {
            this.mockMvc.perform(get("/jobSchedulerEdit")
                    .param("jobConfigurationId", this.addedJobConfiguration.getJobConfigurationId().toString())
                    .param("applicationid", this.applicationId)
            )
                    .andExpect
                            (status().isOk())
                    .andExpect
                            (view().name("jobSchedulerEdit"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void initEditJobListenerConfigurationIT() {
        try {
            this.mockMvc.perform(get("/jobListenerEdit")
                    .param("jobConfigurationId", this.addedListenerJobConfiguration.getJobConfigurationId().toString())
                    .param("applicationid", this.applicationId)
            )
                    .andExpect
                            (status().isOk())
                    .andExpect
                            (view().name("jobListenerEdit"));
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
                    .param("applicationId", this.applicationId)
            )
                    .andExpect
                            (status().isFound());
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void updateJobConfigurationIT() {
        try {
            this.mockMvc.perform(post("/jobConfigurationEdit")
                    .param("jobConfigurationId", this.addedJobConfiguration.getJobConfigurationId().toString())
                    .param("jobIncrementer", "NONE")
                    .param("schedulerStatus", "INITIALIZED")
                    .param("fixedDelay", "100")
                    .param("initialDelay", "1000")
                    .param("jobName", "simpleJob")
                    .param("jobSchedulerType", "PERIOD")
                    .param("taskExecutorType", "SYNCHRONOUS")
                    .param("applicationId", this.applicationId)
            )
                    .andExpect
                            (status().isFound());
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    // FIXME: 31/10/2016
    public void deleteJobConfigurationIT() {
        try {
            this.mockMvc.perform(post("/jobConfigurations")
                    .param("jobConfigurationId", this.addedJobConfiguration.getJobConfigurationId().toString())
                    .param("applicationid", this.applicationId)
            )
                    .andExpect
                            (status().isFound());
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void startJobConfigurationIT() {
        try {
            this.mockMvc.perform(post("/jobConfigurationStart")
                    .param("jobConfigurationId", this.addedJobConfiguration.getJobConfigurationId().toString())
                    .param("applicationid", this.applicationId)
                    .param("redirect", "jobSchedulerConfigurations"))
                    .andExpect
                            (status().isFound());
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void stopJobConfigurationSchedulerIT() {
        try {
            this.mockMvc.perform(post("/jobConfigurationStop")
                    .param("jobConfigurationId", this.addedJobConfiguration.getJobConfigurationId().toString())
                    .param("applicationid", this.applicationId)
                    .param("redirect", "jobSchedulerConfigurations"))
                    .andExpect
                            (status().isFound());
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
