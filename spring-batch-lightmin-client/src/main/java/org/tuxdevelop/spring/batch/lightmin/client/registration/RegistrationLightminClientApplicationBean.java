/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tuxdevelop.spring.batch.lightmin.client.registration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public class RegistrationLightminClientApplicationBean {

    @Setter
    private boolean autoDeregister = false;
    @Setter
    private boolean autoRegister = true;
    @Setter
    private long registerPeriod = 10000L;

    private volatile ScheduledFuture<?> scheduledTask;

    private final LightminClientRegistrator lightminClientRegistrator;
    private final TaskScheduler taskScheduler;


    public RegistrationLightminClientApplicationBean(final LightminClientRegistrator lightminClientRegistrator,
                                                     final TaskScheduler taskScheduler) {
        this.lightminClientRegistrator = lightminClientRegistrator;
        this.taskScheduler = taskScheduler;
    }

    public RegistrationLightminClientApplicationBean(final LightminClientRegistrator lightminClientRegistrator,
                                                     final ScheduledExecutorService scheduler) {
        this(lightminClientRegistrator, new ConcurrentTaskScheduler(scheduler));
    }

    public RegistrationLightminClientApplicationBean(final LightminClientRegistrator lightminClientRegistrator) {
        this(lightminClientRegistrator, Executors.newSingleThreadScheduledExecutor());
    }

    public void startRegisterTask() {
        if (this.autoRegister) {
            if (this.scheduledTask != null && !this.scheduledTask.isDone()) {
                return;
            }
            this.scheduledTask = this.taskScheduler.scheduleAtFixedRate(this.lightminClientRegistrator::register, this.registerPeriod);
            log.debug("Scheduled registration task for every {}ms", this.registerPeriod);
        }
    }

    public void stopRegisterTask() {
        if (this.autoDeregister) {
            if (this.scheduledTask != null && !this.scheduledTask.isDone()) {
                this.scheduledTask.cancel(Boolean.TRUE);
                log.debug("Canceled registration task");
            }
        }
    }
}
