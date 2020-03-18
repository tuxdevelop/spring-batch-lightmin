package org.tuxdevelop.spring.batch.lightmin.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.configuration.LightminMetricsConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {LightminMetricsConfiguration.class})
public @interface EnableLightminMetrics {
}
