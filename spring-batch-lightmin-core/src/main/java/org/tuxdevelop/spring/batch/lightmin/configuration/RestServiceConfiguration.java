package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Configuration
@ComponentScan(basePackages = {"org.tuxdevelop.spring.batch.lightmin.api.controller",
        "org.tuxdevelop.spring.batch.lightmin.support"})
public class RestServiceConfiguration {


}
