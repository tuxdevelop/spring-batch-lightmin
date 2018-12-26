package org.tuxdevelop.spring.batch.lightmin.repository.server.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 0.4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {SpringBatchLightminRemoteRepositoryServerConfiguration.class})
public @interface EnableLightminRepositoryServer {
}
