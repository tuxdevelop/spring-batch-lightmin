package org.tuxdevelop.spring.batch.lightmin.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.test.configuration.ITMapConfiguration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ITMapConfiguration.class)
public class JobLauncherBeanIT {

    @Autowired
    private JobLauncherBean jobLauncherBean;

    @Test
    public void testLaunchJob() {
        final JobParameters jobParameters = new JobParameters();
        final Map<String, JobParameter> jobParametersMap = new HashMap<>();
        final JobParameter jobParameter = new JobParameter();
        jobParameter.setParameterType(ParameterType.DATE);
        jobParameter.setParameter(new Date());
        jobParametersMap.put("someDate", jobParameter);
        jobParameters.setParameters(jobParametersMap);
        final JobLaunch jobLaunch = new JobLaunch();
        jobLaunch.setJobName("simpleJob");
        jobLaunch.setJobParameters(jobParameters);
        this.jobLauncherBean.launchJob(jobLaunch);
    }

}
