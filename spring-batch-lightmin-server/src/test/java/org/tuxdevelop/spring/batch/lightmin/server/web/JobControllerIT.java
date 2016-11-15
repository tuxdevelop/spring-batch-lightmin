package org.tuxdevelop.spring.batch.lightmin.server.web;


import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class JobControllerIT extends CommonControllerIT {

    @Test
    public void initJobsIT() {
        try {
            this.mockMvc.perform(get("/jobs")
                    .param("applicationid", applicationId)
            )
                    .andExpect(status().isOk())
                    .andExpect(view().name("jobs"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getJobIT() {
        try {
            this.mockMvc.perform(get("/job")
                    .param("jobname", "simpleJob")
                    .param("applicationid", applicationId)
            ).andExpect(status().isOk())
                    .andExpect(view().name
                            ("job"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getJobExecutionsIT() {
        try {
            this.mockMvc.perform(get("/executions")
                    .param("jobInstanceId", launchedJobInstanceId.toString())
                    .param("applicationid", applicationId)
            ).andExpect(status().isOk()).andExpect(view().name
                    ("jobExecutions"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getJobExecutionIT() {
        try {
            this.mockMvc.perform(get("/execution")
                    .param("jobExecutionId", launchedJobExecutionId.toString())
                    .param("applicationid", applicationId)
            ).andExpect(status().isOk()).andExpect(view().name("jobExecution"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    //Restart not possible
    public void restartJobExecutionIT() {
        try {
            this.mockMvc.perform(post("/executionRestart")
                    .param("jobExecutionId", launchedJobExecutionId.toString())
                    .param("jobInstanceId", launchedJobInstanceId.toString())
                    .param("applicationid", applicationId)
            ).andExpect(status().isFound()).andExpect(view().name
                    ("redirect:executions?jobInstanceId=" + launchedJobInstanceId + "&applicationid=" +
                            applicationId));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void stopJobExecutionIT() {
        try {
            this.mockMvc.perform(post("/executionStop")
                    .param("jobExecutionId", launchedJobExecutionId.toString())
                    .param("jobInstanceId", launchedJobInstanceId.toString())
                    .param("applicationid", applicationId)
            ).andExpect(status().isOk()).andExpect(view().name
                    ("error"));
            //error because job not running
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
