
package org.tuxdevelop.spring.batch.lightmin.client.listener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.MetricEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.RemoteJobExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.client.publisher.RemoteStepExecutionEventPublisher;
import org.tuxdevelop.spring.batch.lightmin.event.JobExecutionEvent;

import static org.mockito.Mockito.any;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest()
public class OnJobExecutionFinishedEventListenerTest {

    private OnJobExecutionFinishedEventListener onJobExecutionFinishedEventListener;

    @Mock
    private RemoteJobExecutionEventPublisher jobExecutionEventPublisher;

    @Mock
    private RemoteStepExecutionEventPublisher remoteStepExecutionEventPublisher;

    @Mock
    private MetricEventPublisher metricEventPublisher;

    @Test
    public void testOnApplicationEventJobExecution() {
        final JobInstance instance = new JobInstance(1L, "testJob");
        final JobExecution jobExecution = new JobExecution(1L);
        jobExecution.setJobInstance(instance);
        jobExecution.setExitStatus(ExitStatus.COMPLETED);
        final JobExecutionEvent jobExecutionEvent = new JobExecutionEvent(jobExecution, "testApplication");

        this.onJobExecutionFinishedEventListener.onApplicationEvent(jobExecutionEvent);
        Mockito.verify(this.jobExecutionEventPublisher, Mockito.times(1))
                .publishEvent(any(JobExecutionEventInfo.class));
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.onJobExecutionFinishedEventListener = new OnJobExecutionFinishedEventListener(this.jobExecutionEventPublisher, remoteStepExecutionEventPublisher, metricEventPublisher);
    }

}

