package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {CommonSpringBatchLightminConfiguration.class})
public @interface EnableSpringBatchLightmin {
}
