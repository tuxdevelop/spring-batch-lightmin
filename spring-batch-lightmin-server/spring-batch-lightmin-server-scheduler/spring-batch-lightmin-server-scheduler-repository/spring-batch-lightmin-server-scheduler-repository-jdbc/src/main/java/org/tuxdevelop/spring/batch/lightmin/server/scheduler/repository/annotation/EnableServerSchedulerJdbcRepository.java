package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.configuration.ServerSchedulerJdbcConfiguration;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {ServerSchedulerJdbcConfiguration.class})
public @interface EnableServerSchedulerJdbcRepository {
}
