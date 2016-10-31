package org.tuxdevelop.spring.batch.lightmin.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tuxdevelop.spring.batch.lightmin.TestHelper;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.test.configuration.ITMapConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ITMapConfiguration.class)
public class ControllerServiceEntryBeanIT {

    @Autowired
    private ServiceEntry serviceEntry;


    @Test
    public void testSaveJobConfiguration() {
        final String jobName = "simpleJob";
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(jobName);
        serviceEntry.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add(jobName);
        final JobConfigurations jobConfigurations = serviceEntry.getJobConfigurations(jobNames);
        assertThat(jobConfigurations).isNotNull();
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).isNotEmpty();
    }

    @Test
    public void testUpdateJobConfiguration() {
        final String jobName = "simpleJob";
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(jobName);
        serviceEntry.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add(jobName);
        final JobConfigurations jobConfigurations = serviceEntry.getJobConfigurations(jobNames);
        assertThat(jobConfigurations).isNotNull();
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).isNotEmpty();
        Long jobConfigurationId = null;
        for (final JobConfiguration fetchedJobConfiguration : fetchedJobConfigurations) {
            jobConfigurationId = fetchedJobConfiguration.getJobConfigurationId();
        }
        assertThat(jobConfigurationId).isNotNull();
        final JobConfiguration fetchedJobConfiguration = serviceEntry.getJobConfigurationById(jobConfigurationId);
        assertThat(fetchedJobConfiguration).isNotNull();
        final JobParameters jobParameters = new JobParameters();
        final Map<String, JobParameter> jobParametersMap = new HashMap<>();
        final JobParameter jobParameter = new JobParameter();
        jobParameter.setParameter(20.2);
        jobParameter.setParameterType(ParameterType.DOUBLE);
        jobParametersMap.put("Double", jobParameter);
        jobParameters.setParameters(jobParametersMap);
        fetchedJobConfiguration.setJobParameters(jobParameters);
        serviceEntry.updateJobConfiguration(fetchedJobConfiguration);
        final JobConfiguration updatedJobConfiguration = serviceEntry.getJobConfigurationById(jobConfigurationId);
        assertThat(updatedJobConfiguration).isEqualTo(fetchedJobConfiguration);
    }

    @Test
    public void testDeleteJobConfiguration() {
        final String jobName = "simpleJob";
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(jobName);
        serviceEntry.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add(jobName);
        final JobConfigurations jobConfigurations = serviceEntry.getJobConfigurations(jobNames);
        assertThat(jobConfigurations).isNotNull();
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).hasSize(1);
        final Long jobConfigurationId = fetchedJobConfigurations.iterator().next().getJobConfigurationId();
        serviceEntry.deleteJobConfiguration(jobConfigurationId);
        final JobConfigurations jobConfigurationsAfterDelete = serviceEntry.getJobConfigurations(jobNames);
        assertThat(jobConfigurationsAfterDelete.getJobConfigurations()).isEmpty();
    }

    @Test
    public void testGetJobConfigurationsByJobName() {
        final String jobName = "simpleJob";
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(jobName);
        serviceEntry.saveJobConfiguration(jobConfiguration);
        final JobConfigurations fetchedJobConfigurations = serviceEntry.getJobConfigurationsByJobName(jobName);
        final Collection<JobConfiguration> fetchedJobConfigurationsCollection = fetchedJobConfigurations
                .getJobConfigurations();
        assertThat(fetchedJobConfigurationsCollection).isNotEmpty();
        final JobConfiguration jobConfigurationResult = fetchedJobConfigurationsCollection.iterator().next();
        assertThat(jobConfigurationResult.getJobName()).isEqualTo(jobName);
    }

    @Test
    public void testGetJobConfigurationMap() {
        final String jobName = "simpleJob";
        final JobSchedulerConfiguration jobSchedulerConfiguration = TestHelper.createJobSchedulerConfiguration(null, 10L, 10L,
                JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = TestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName(jobName);
        serviceEntry.saveJobConfiguration(jobConfiguration);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add(jobName);
        final Map<String, JobConfigurations> result = serviceEntry.getJobConfigurationMap(jobNames);
        assertThat(result.containsKey(jobName));
        final JobConfigurations jobConfigurations = result.get(jobName);
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).isNotEmpty();
        final JobConfiguration jobConfigurationResult = fetchedJobConfigurations.iterator().next();
        assertThat(jobConfigurationResult.getJobName()).isEqualTo(jobName);
    }

}
