package org.tuxdevelop.spring.batch.lightmin.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfiguration;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {SpringBatchLightminConfiguration.class})
public @interface EnableLightminCore {
}
