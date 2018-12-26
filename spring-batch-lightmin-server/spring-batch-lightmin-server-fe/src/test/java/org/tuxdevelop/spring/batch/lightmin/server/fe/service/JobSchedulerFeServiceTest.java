package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfigurations;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.MapJobSchedulerConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration.SchedulerJobConfigurationModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.AdminServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.test.api.ApiTestHelper;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JobSchedulerFeServiceTest {

    private static final String APPLICATION_INSTANCE_ID = "testApp";
    private static final Long JC_ID = 1L;

    private JobSchedulerFeService jobSchedulerFeService;
    @Mock
    private RegistrationBean registrationBean;
    @Mock
    private AdminServerService adminServerService;

    @Test
    public void testGetMapJobConfigurationModel() {

        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        final Map<String, JobConfigurations> jobConfigurationsMap = new HashMap<>();

        ApiTestHelper.addJobConfigurations(jobConfigurationsMap,
                APPLICATION_INSTANCE_ID,
                ApiTestHelper.createJobSchedulerConfigurations(5));

        when(this.adminServerService.getJobConfigurationsMap(lightminClientApplication)).thenReturn(jobConfigurationsMap);

        final MapJobSchedulerConfigurationModel result =
                this.jobSchedulerFeService.getMapJobConfigurationModel(APPLICATION_INSTANCE_ID);


        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getJobConfigurations()).isNotNull();
        BDDAssertions.then(result.getJobConfigurations().size()).isEqualTo(1);

    }

    @Test
    public void testGetJobConfigurationModel() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        final JobConfiguration jobConfiguration = ApiTestHelper.createJobConfiguration(
                ApiTestHelper.createJobSchedulerConfiguration(null, 1L, 1L, JobSchedulerType.PERIOD));

        when(this.adminServerService.getJobConfiguration(JC_ID, lightminClientApplication)).thenReturn(jobConfiguration);

        final SchedulerJobConfigurationModel result =
                this.jobSchedulerFeService.getJobConfigurationModel(JC_ID, APPLICATION_INSTANCE_ID);

        BDDAssertions.then(result).isNotNull();
    }

    @Test
    public void testStartListener() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        try {
            this.jobSchedulerFeService.startScheduler(JC_ID, APPLICATION_INSTANCE_ID);
            verify(this.adminServerService, times(1)).startJobConfigurationScheduler(JC_ID, lightminClientApplication);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testStopListener() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        try {
            this.jobSchedulerFeService.stopScheduler(JC_ID, APPLICATION_INSTANCE_ID);
            verify(this.adminServerService, times(1)).stopJobConfigurationScheduler(JC_ID, lightminClientApplication);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteListenerConfiguration() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        try {
            this.jobSchedulerFeService.deleteSchedulerConfiguration(JC_ID, APPLICATION_INSTANCE_ID);
            verify(this.adminServerService, times(1)).deleteJobConfiguration(JC_ID, lightminClientApplication);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddListenerConfiguration() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        final SchedulerJobConfigurationModel schedulerJobConfigurationModel =
                ServiceTestHelper.createSchedulerJobConfigurationModel("testJob");

        try {
            this.jobSchedulerFeService.addSchedulerConfiguration(schedulerJobConfigurationModel, APPLICATION_INSTANCE_ID);
            verify(this.adminServerService, times(1)).
                    saveJobConfiguration(this.jobSchedulerFeService.map(
                            schedulerJobConfigurationModel), lightminClientApplication);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateListenerConfiguration() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_INSTANCE_ID)).thenReturn(lightminClientApplication);

        final SchedulerJobConfigurationModel schedulerJobConfigurationModel =
                ServiceTestHelper.createSchedulerJobConfigurationModel("testJob");

        try {
            this.jobSchedulerFeService.updateSchedulerConfiguration(schedulerJobConfigurationModel, APPLICATION_INSTANCE_ID);
            verify(this.adminServerService, times(1)).
                    updateJobConfiguration(this.jobSchedulerFeService.map(
                            schedulerJobConfigurationModel), lightminClientApplication);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.jobSchedulerFeService = new JobSchedulerFeService(this.registrationBean, this.adminServerService);
    }
}
