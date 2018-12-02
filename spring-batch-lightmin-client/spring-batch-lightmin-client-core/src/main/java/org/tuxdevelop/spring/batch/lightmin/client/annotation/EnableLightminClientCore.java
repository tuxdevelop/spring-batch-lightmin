package org.tuxdevelop.spring.batch.lightmin.client.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.client.configuration.LightminClientConfiguration;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {LightminClientConfiguration.class})
public @interface EnableLightminClientCore {
}
