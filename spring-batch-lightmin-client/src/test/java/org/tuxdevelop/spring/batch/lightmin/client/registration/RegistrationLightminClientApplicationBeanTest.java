package org.tuxdevelop.spring.batch.lightmin.client.registration;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.TaskScheduler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RegistrationLightminClientApplicationBeanTest {

    @Mock
    private ScheduledFuture<?> scheduledTask;
    @Mock
    private TaskScheduler taskScheduler;
    @Mock
    private LightminClientRegistrator lightminClientRegistrator;

    //    @InjectMocks
    private RegistrationLightminClientApplicationBean registrationLightminClientApplicationBean;

    @Test
    public void testStartRegisterTask() {
        registrationLightminClientApplicationBean.startRegisterTask();
        verify(taskScheduler, times(1)).scheduleAtFixedRate(any(Runnable.class), anyLong());
    }

    @Test
    public void testStopRegisterTask() {
        final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
        final ScheduledFuture future = executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

            }
        }, 1, 1, TimeUnit.DAYS);
        when(taskScheduler.scheduleAtFixedRate(any(Runnable.class), anyLong())).thenReturn(future);
        registrationLightminClientApplicationBean.startRegisterTask();
        verify(taskScheduler, times(1)).scheduleAtFixedRate(any(Runnable.class), anyLong());
        registrationLightminClientApplicationBean.setAutoDeregister(Boolean.TRUE);
        when(scheduledTask.isDone()).thenReturn(Boolean.FALSE);
        registrationLightminClientApplicationBean.stopRegisterTask();
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        registrationLightminClientApplicationBean = new RegistrationLightminClientApplicationBean(lightminClientRegistrator, taskScheduler);
    }
}
