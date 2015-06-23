package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {AbstractSpringBatchLightminConfiguration.class, SpringBatchLightminWebConfiguration.class})
public @interface EnableSpringBatchLingtminUI {
}
