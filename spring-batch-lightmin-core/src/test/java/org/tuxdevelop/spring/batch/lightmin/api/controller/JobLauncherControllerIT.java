package org.tuxdevelop.spring.batch.lightmin.api.controller;


import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JobLauncherControllerIT extends CommonControllerIT {

    @Test
    public void testLaunchJob() {
        final String jobName = "simpleJob";
        final String uri = LOCALHOST + ":" + getServerPort() + AbstractRestController.JobLauncherRestControllerAPI.JOB_LAUNCH;
        final JobParameters jobParameters = new JobParameters();
        final JobParameter jobParameter = new JobParameter();
        jobParameter.setParameter(10.1);
        jobParameter.setParameterType(ParameterType.DOUBLE);
        final Map<String, JobParameter> map = new HashMap<>();
        map.put("doubleValue", jobParameter);
        jobParameters.setParameters(map);
        final JobLaunch jobLaunch = new JobLaunch();
        jobLaunch.setJobName(jobName);
        jobLaunch.setJobParameters(jobParameters);
        final ResponseEntity<Void> response = restTemplate.postForEntity(uri, jobLaunch, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

}
