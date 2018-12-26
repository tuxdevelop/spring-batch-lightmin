package org.tuxdevelop.spring.batch.lightmin.batch.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.batch.configuration.SpringBatchLightminBatchConfiguration;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {SpringBatchLightminBatchConfiguration.class})
public @interface EnableLightminBatch {
}
