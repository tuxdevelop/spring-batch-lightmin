package org.tuxdevelop.spring.batch.lightmin.client.configuration;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.configuration.EnableSpringBatchLightmin;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableSpringBatchLightmin
@Import(value = LightminClientConfiguration.class)
public @interface EnableSpringBatchLightminClient {
}
