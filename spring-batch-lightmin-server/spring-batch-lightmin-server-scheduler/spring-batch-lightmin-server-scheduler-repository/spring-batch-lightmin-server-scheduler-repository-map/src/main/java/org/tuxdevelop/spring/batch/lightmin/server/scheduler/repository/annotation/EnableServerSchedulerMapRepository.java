package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.configuration.ServerSchedulerMapConfiguration;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {ServerSchedulerMapConfiguration.class})
public @interface EnableServerSchedulerMapRepository {
}
