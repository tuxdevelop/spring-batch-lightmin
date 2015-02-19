package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.tuxdevelop.spring.batch.lightmin.configuration.AbstractSpringBatchLightminConfiguration;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminWebConfiguration;

@Configuration
@ComponentScan(basePackages = "org.tuxdevelop.spring.batch.lightmin", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SpringBatchLightminWebConfiguration.class)})
public class ITConfiguration extends AbstractSpringBatchLightminConfiguration {


}
