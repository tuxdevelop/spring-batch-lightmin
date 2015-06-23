package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AbstractSpringBatchLightminConfiguration.class)
public @interface EnableSpringBatchLingtmin {
}
