package org.tuxdevelop.spring.batch.lightmin.server.cluster.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.cluster.configuration.CommonServerClusterConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {CommonServerClusterConfiguration.class})
public @interface EnableServerClusterCore {
}
