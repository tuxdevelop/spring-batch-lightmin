package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ContentPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.event.JobExecutionEventModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobExecutionEventFeServiceTest {

    private JobExecutionEventFeService jobExecutionEventFeService;
    @Mock
    private RegistrationBean registrationBean;
    @Mock
    private EventService eventService;

    @Test
    public void testGetJobExecutionEventModels() {
        final int pageSize = 5;
        final List<JobExecutionEventInfo> jobExecutionEventInfos =
                ServiceTestHelper.createJobExecutionEvents(pageSize, "test_application");
        when(this.eventService.getAllJobExecutionEvents(0, pageSize)).thenReturn(jobExecutionEventInfos);

        final ContentPageModel<List<JobExecutionEventModel>> result =
                this.jobExecutionEventFeService.getJobExecutionEventModels(0, pageSize);

        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getValue()).isNotNull();
        BDDAssertions.then(result.getValue().size()).isEqualTo(pageSize);
    }


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.jobExecutionEventFeService = new JobExecutionEventFeService(this.registrationBean, this.eventService);
    }

}
