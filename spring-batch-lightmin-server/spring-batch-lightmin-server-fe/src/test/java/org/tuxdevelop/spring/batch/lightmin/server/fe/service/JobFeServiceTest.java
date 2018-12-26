package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.BatchJobInfoModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.test.api.ApiTestHelper;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobFeServiceTest {

    private static final String APPLICATION_ID = "test_app";

    private JobFeService jobFeService;
    @Mock
    private RegistrationBean registrationBean;
    @Mock
    private JobServerService jobServerService;

    @Test
    public void testFindById() {
        final LightminClientApplication lightminClientApplication = ServiceTestHelper.createLightminClientApplication();
        when(this.registrationBean.findById(APPLICATION_ID)).thenReturn(lightminClientApplication);
        when(this.jobServerService.getJobInfo(anyString(), any())).thenReturn(ApiTestHelper.createJobInfo("test_job", 2));
        final List<BatchJobInfoModel> result = this.jobFeService.findById(APPLICATION_ID);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.size()).isEqualTo(2);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.jobFeService = new JobFeService(this.jobServerService, this.registrationBean);

    }
}
