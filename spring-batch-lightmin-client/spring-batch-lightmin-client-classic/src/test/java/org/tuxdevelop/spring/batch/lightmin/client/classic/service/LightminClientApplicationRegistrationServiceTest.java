package org.tuxdevelop.spring.batch.lightmin.client.classic.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.TaskScheduler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LightminClientApplicationRegistrationServiceTest {

    @Mock
    private ScheduledFuture<?> scheduledTask;
    @Mock
    private TaskScheduler taskScheduler;
    @Mock
    private LightminClientRegistratorService lightminClientRegistratorService;

    @InjectMocks
    private LightminClientApplicationRegistrationService lightminClientApplicationRegistrationService;

    @Test
    public void testStartRegisterTask() {
        this.lightminClientApplicationRegistrationService.startRegisterTask();
        verify(this.taskScheduler, times(1)).scheduleAtFixedRate(any(Runnable.class), anyLong());
    }

    @Test
    public void testStopRegisterTask() {
        final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
        final ScheduledFuture future = executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

            }
        }, 1, 1, TimeUnit.DAYS);
        when(this.taskScheduler.scheduleAtFixedRate(any(Runnable.class), anyLong())).thenReturn(future);
        this.lightminClientApplicationRegistrationService.startRegisterTask();
        verify(this.taskScheduler, times(1)).scheduleAtFixedRate(any(Runnable.class), anyLong());
        this.lightminClientApplicationRegistrationService.setAutoDeregister(Boolean.TRUE);
        when(this.scheduledTask.isDone()).thenReturn(Boolean.FALSE);
        this.lightminClientApplicationRegistrationService.stopRegisterTask();
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.lightminClientApplicationRegistrationService =
                new LightminClientApplicationRegistrationService(
                        this.lightminClientRegistratorService, this.taskScheduler);
    }
}
