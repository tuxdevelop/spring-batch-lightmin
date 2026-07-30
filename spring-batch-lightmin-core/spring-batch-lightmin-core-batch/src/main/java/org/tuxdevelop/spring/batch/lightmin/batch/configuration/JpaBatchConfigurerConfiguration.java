package org.tuxdevelop.spring.batch.lightmin.batch.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Enables the common Batch 6 infrastructure for JPA applications.  The common
 * configuration detects the available EntityManagerFactory and supplies a JPA
 * transaction manager to the repository factory.
 */
@Configuration
@Import(SpringBatchLightminBatchConfiguration.class)
public class JpaBatchConfigurerConfiguration {
}
