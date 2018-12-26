package org.tuxdevelop.spring.batch.lightmin.server.fe.annotation;

import org.springframework.context.annotation.Import;
import org.tuxdevelop.spring.batch.lightmin.server.fe.configuration.LightminServerFeConfiguration;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {LightminServerFeConfiguration.class})
public @interface EnableLightminServerFrontend {
}
