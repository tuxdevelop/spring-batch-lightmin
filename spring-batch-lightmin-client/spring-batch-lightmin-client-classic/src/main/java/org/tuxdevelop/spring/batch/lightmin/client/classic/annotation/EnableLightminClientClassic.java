package org.tuxdevelop.spring.batch.lightmin.client.classic.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.client.classic.configuration.ClassicLightminClientConfiguration;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {ClassicLightminClientConfiguration.class})
public @interface EnableLightminClientClassic {
}
