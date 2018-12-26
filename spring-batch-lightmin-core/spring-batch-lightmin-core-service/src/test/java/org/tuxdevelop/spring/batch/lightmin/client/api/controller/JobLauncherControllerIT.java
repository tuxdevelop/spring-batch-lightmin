package org.tuxdevelop.spring.batch.lightmin.client.api.controller;


import org.junit.Before;
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
        final String uri = LOCALHOST + ":" + this.getServerPort() + AbstractRestController.JobLauncherRestControllerAPI.JOB_LAUNCH;
        final JobParameters jobParameters = new JobParameters();
        final JobParameter jobParameter = new JobParameter();
        jobParameter.setParameter(10.1);
        jobParameter.setParameterType(ParameterType.DOUBLE);
        final JobParameter jobParameterDate = new JobParameter();
        jobParameterDate.setParameter("2017/02/10 13:42:00:001");
        jobParameterDate.setParameterType(ParameterType.DATE);
        final JobParameter jobParameterLong = new JobParameter();
        jobParameterLong.setParameter(10L);
        jobParameterLong.setParameterType(ParameterType.LONG);
        final JobParameter jobParameterInteger = new JobParameter();
        jobParameterInteger.setParameter(10);
        jobParameterInteger.setParameterType(ParameterType.LONG);
        final JobParameter jobParameterString = new JobParameter();
        jobParameterString.setParameter("testString");
        jobParameterString.setParameterType(ParameterType.STRING);
        final JobParameter jobParameterInc = new JobParameter();
        jobParameterInc.setParameter(System.currentTimeMillis());
        jobParameterInc.setParameterType(ParameterType.LONG);
        final Map<String, JobParameter> map = new HashMap<>();
        map.put("doubleValue", jobParameter);
        map.put("dateValue", jobParameterDate);
        map.put("longValue", jobParameterLong);
        map.put("integerValue", jobParameterInteger);
        map.put("stringValue", jobParameterString);
        map.put("incrementer", jobParameterInc);
        jobParameters.setParameters(map);
        final JobLaunch jobLaunch = new JobLaunch();
        jobLaunch.setJobName(jobName);
        jobLaunch.setJobParameters(jobParameters);

        final ResponseEntity<Void> response = this.restTemplate.postForEntity(uri, jobLaunch, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Before
    public void init() {
        this.cleanUp();
    }
}
