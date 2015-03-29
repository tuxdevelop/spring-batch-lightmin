package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SpringBatchLightminWebConfiguration {

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(final ViewControllerRegistry viewControllerRegistry) {
                viewControllerRegistry.addViewController("/").setViewName("index");
                viewControllerRegistry.addViewController("index").setViewName("index");
                viewControllerRegistry.addViewController("jobs").setViewName("jobs");
                viewControllerRegistry.addViewController("jobs/{jobName}").setViewName("job");
                viewControllerRegistry.addViewController("jobs/executions/{jobInstanceId}")
                        .setViewName("jobExecutions");
                viewControllerRegistry.addViewController("jobs/execution/{jobExecutionId}").setViewName("jobExecution");
                viewControllerRegistry.addViewController("admin").setViewName("admin");
                viewControllerRegistry.addViewController("jobConfigurations").setViewName("jobConfigurations");
                viewControllerRegistry.addViewController("jobConfigurationAdd").setViewName("jobConfigurationAdd");
                viewControllerRegistry.addViewController("jobConfigurationEdit").setViewName("jobConfigurationEdit");
                viewControllerRegistry.addViewController("jobConfiguration").setViewName("jobConfiguration");
                viewControllerRegistry.addViewController("jobLaunchers").setViewName("jobLaunchers");
                viewControllerRegistry.addViewController("jobConfigurations/delete/{jobConfigurationID}").setViewName
                        ("jobConfigurations");
            }
        };
    }
}
