package org.tuxdevelop.spring.batch.lightmin.client.event.listener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.tuxdevelop.spring.batch.lightmin.admin.event.JobExecutionEvent;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.event.JobExecutionEventPublisher;

import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class OnJobExecutionFinishedEventListenerTest {

    private OnJobExecutionFinishedEventListener onJobExecutionFinishedEventListener;

    @Mock
    private JobExecutionEventPublisher jobExecutionEventPublisher;

    @Test
    public void testOnApplicationEventJobExecution() {
        final JobInstance instance = new JobInstance(1L, "testJob");
        final JobExecution jobExecution = new JobExecution(1L);
        jobExecution.setJobInstance(instance);
        jobExecution.setExitStatus(ExitStatus.COMPLETED);
        final JobExecutionEvent jobExecutionEvent = new JobExecutionEvent(jobExecution, "testApplication");
        onJobExecutionFinishedEventListener.onApplicationEvent(jobExecutionEvent);
        Mockito.verify(jobExecutionEventPublisher, Mockito.times(1))
                .publishJobExecutionEvent(any(JobExecutionEventInfo.class));
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        onJobExecutionFinishedEventListener = new OnJobExecutionFinishedEventListener(jobExecutionEventPublisher);
    }
}
