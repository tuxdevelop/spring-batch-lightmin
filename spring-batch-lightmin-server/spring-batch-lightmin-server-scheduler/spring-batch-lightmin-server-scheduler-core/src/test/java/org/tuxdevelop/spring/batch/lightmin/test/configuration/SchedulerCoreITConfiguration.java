package org.tuxdevelop.spring.batch.lightmin.test.configuration;

import org.springframework.context.annotation.Configuration;
import org.tuxdevelop.spring.batch.lightmin.server.annotation.EnableLightminServerCore;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.annotation.EnableServerSchedulerCore;

@Configuration
@EnableServerSchedulerCore
@EnableLightminServerCore
public class SchedulerCoreITConfiguration {
}
