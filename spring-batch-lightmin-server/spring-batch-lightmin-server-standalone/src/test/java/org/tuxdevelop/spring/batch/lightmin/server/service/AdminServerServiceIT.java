package org.tuxdevelop.spring.batch.lightmin.server.service;


import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.test.api.ApiTestHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AdminServerServiceIT {

    @Test
    public void testSaveJobConfiguration() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication();
        final JobConfiguration jobConfiguration = this.createJobConfiguration();
        this.getAdminServerService().saveJobConfiguration(jobConfiguration, lightminClientApplication);
        final JobConfigurations result = this.getAdminServerService().getJobConfigurations(lightminClientApplication);
        assertThat(result).isNotNull();
        assertThat(result.getJobConfigurations()).isNotEmpty();
    }

    @Test
    public void testUpdateJobConfiguration() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication();
        final JobConfiguration jobConfiguration = this.createJobConfiguration();
        this.getAdminServerService().saveJobConfiguration(jobConfiguration, lightminClientApplication);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add("simpleJob");
        final JobConfigurations jobConfigurations = this.getAdminServerService().getJobConfigurations(lightminClientApplication);
        assertThat(jobConfigurations).isNotNull();
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).isNotEmpty();
        Long jobConfigurationId = null;
        for (final JobConfiguration fetchedJobConfiguration : fetchedJobConfigurations) {
            jobConfigurationId = fetchedJobConfiguration.getJobConfigurationId();
        }
        assertThat(jobConfigurationId).isNotNull();
        final JobConfiguration fetchedJobConfiguration = this.getAdminServerService().getJobConfiguration(jobConfigurationId, lightminClientApplication);
        assertThat(fetchedJobConfiguration).isNotNull();
        final JobParameters jobParameters = new JobParameters();
        final Map<String, JobParameter> jobParametersMap = new HashMap<>();
        final JobParameter jobParameter = new JobParameter();
        jobParameter.setParameter(20.2);
        jobParameter.setParameterType(ParameterType.DOUBLE);
        jobParametersMap.put("Double", jobParameter);
        jobParameters.setParameters(jobParametersMap);
        fetchedJobConfiguration.setJobParameters(jobParameters);
        this.getAdminServerService().updateJobConfiguration(fetchedJobConfiguration, lightminClientApplication);
        final JobConfiguration updatedJobConfiguration = this.getAdminServerService().getJobConfiguration(jobConfigurationId, lightminClientApplication);
        assertThat(updatedJobConfiguration).isEqualTo(fetchedJobConfiguration);
    }

    @Test
    public void testDeleteJobConfiguration() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication();
        final JobConfiguration jobConfiguration = this.createJobConfiguration();
        this.getAdminServerService().saveJobConfiguration(jobConfiguration, lightminClientApplication);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add("simpleJob");
        final JobConfigurations jobConfigurations = this.getAdminServerService().getJobConfigurations(lightminClientApplication);
        assertThat(jobConfigurations).isNotNull();
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).isNotEmpty();
        final Long jobConfigurationId = fetchedJobConfigurations.iterator().next().getJobConfigurationId();
        this.getAdminServerService().deleteJobConfiguration(jobConfigurationId, lightminClientApplication);
        final JobConfigurations jobConfigurationsAfterDelete = this.getAdminServerService().getJobConfigurations(lightminClientApplication);
        assertThat(jobConfigurationsAfterDelete.getJobConfigurations()).hasSize(fetchedJobConfigurations.size() - 1);
    }


    @Test
    public void testGetJobConfigurationMap() {
        final LightminClientApplication lightminClientApplication = this.createLightminClientApplication();
        final JobConfiguration jobConfiguration = this.createJobConfiguration();
        this.getAdminServerService().saveJobConfiguration(jobConfiguration, lightminClientApplication);
        final Collection<String> jobNames = new LinkedList<>();
        jobNames.add("simpleJob");
        final Map<String, JobConfigurations> result = this.getAdminServerService().getJobConfigurationsMap(lightminClientApplication);
        assertThat(result.containsKey("simpleJob"));
        final JobConfigurations jobConfigurations = result.get("simpleJob");
        final Collection<JobConfiguration> fetchedJobConfigurations = jobConfigurations.getJobConfigurations();
        assertThat(fetchedJobConfigurations).isNotEmpty();
        final JobConfiguration jobConfigurationResult = fetchedJobConfigurations.iterator().next();
        assertThat(jobConfigurationResult.getJobName()).isEqualTo("simpleJob");
    }


    public abstract AdminServerService getAdminServerService();

    public abstract LightminClientApplication createLightminClientApplication();

    private JobConfiguration createJobConfiguration() {
        final JobSchedulerConfiguration jobSchedulerConfiguration = ApiTestHelper.createJobSchedulerConfiguration(null, 10L, 10L, JobSchedulerType.PERIOD);
        final JobConfiguration jobConfiguration = ApiTestHelper.createJobConfiguration(jobSchedulerConfiguration);
        jobConfiguration.setJobName("simpleJob");
        return jobConfiguration;
    }


}
