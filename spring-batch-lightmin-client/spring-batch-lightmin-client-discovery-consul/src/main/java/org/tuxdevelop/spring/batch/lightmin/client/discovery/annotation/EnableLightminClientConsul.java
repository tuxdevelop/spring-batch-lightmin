package org.tuxdevelop.spring.batch.lightmin.client.discovery.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.configuration.ConsulLightminClientDiscoveryConfiguration;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {ConsulLightminClientDiscoveryConfiguration.class})
public @interface EnableLightminClientConsul {
}
