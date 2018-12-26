package org.tuxdevelop.spring.batch.lightmin.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.batch.annotation.EnableLightminBatch;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLigthminCoreConfiguration;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableLightminBatch
@Import(value = {SpringBatchLigthminCoreConfiguration.class})
public @interface EnableLightminCoreConfiguration {
}
