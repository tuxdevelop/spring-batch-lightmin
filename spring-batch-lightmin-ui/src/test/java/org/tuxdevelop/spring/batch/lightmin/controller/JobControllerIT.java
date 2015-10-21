package org.tuxdevelop.spring.batch.lightmin.controller;


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
            this.mockMvc.perform(get("/jobs")).andExpect(status().isOk()).andExpect(view().name("jobs"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getJobIT() {
        try {
            this.mockMvc.perform(get("/job?jobname=simpleJob")).andExpect(status().isOk()).andExpect(view().name
                    ("job"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getJobExecutionsIT() {
        try {
            this.mockMvc.perform(get("/executions?jobInstanceId=" + launchedJobInstanceId)).andExpect(status().isOk()).andExpect(view().name
                    ("jobExecutions"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getJobExecutionIT() {
        try {
            this.mockMvc.perform(get("/execution?jobExecutionId=" + launchedJobExecutionId)).andExpect(status().isOk()).andExpect(view().name("jobExecution"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void restartJobExecutionIT() {
        try {
            this.mockMvc.perform(post("/executionRestart?jobExecutionId=" + launchedJobExecutionId + "&" +
                    "jobInstanceId=" + launchedJobInstanceId)).andExpect(status().isFound()).andExpect(view().name
                    ("redirect:executions?jobInstanceId=" + launchedJobInstanceId));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void stopJobExecutionIT() {
        try {
            this.mockMvc.perform(post("/executionStop?jobExecutionId=" + launchedJobExecutionId + "&" +
                    "jobInstanceId=" + launchedJobInstanceId)).andExpect(status().isOk()).andExpect(view().name
                    ("error"));
            //error because job not running
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
