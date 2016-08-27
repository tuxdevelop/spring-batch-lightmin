package org.tuxdevelop.spring.batch.lightmin.server.web;


import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class StepControllerIT extends CommonControllerIT {

    @Test
    @Ignore
    //TODO: fixme
    public void initStepExecutionIT() {
        try {
            this.mockMvc
                    .perform(get("/steps/execution/" + launchedStepExecutionId)
                                    .param("jobName", "simpleJob")
                                    .param("duration", "1000")
                            //.param("jobExecution.id", launchedJobExecutionId.toString())
                    )
                    .andExpect(status().isOk())
                    .andExpect(view().name("stepExecution"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
