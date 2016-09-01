package org.tuxdevelop.spring.batch.lightmin.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.tuxdevelop.spring.batch.lightmin.server.web.*;

@Configuration
@Import(value = {AdminController.class, IndexController.class, JobConfigurationController.class, JobController.class,
        JobLauncherController.class, StepController.class, ApplicationController.class})
public class SpringBatchLightminWebConfiguration {

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(final ViewControllerRegistry viewControllerRegistry) {
                viewControllerRegistry.addViewController("/").setViewName("index");
                viewControllerRegistry.addViewController("/admin").setViewName("admin");
                viewControllerRegistry.addViewController("/index").setViewName("index");
                viewControllerRegistry.addViewController("/jobs").setViewName("jobs");
                viewControllerRegistry.addViewController("/job").setViewName("job");
                viewControllerRegistry.addViewController("/executions").setViewName("jobExecutions");
                viewControllerRegistry.addViewController("/execution").setViewName("jobExecution");
                viewControllerRegistry.addViewController("/executionRestart").setViewName("jobExecutions");
                viewControllerRegistry.addViewController("/executionStop").setViewName("jobExecutions");
                viewControllerRegistry.addViewController("/jobConfigurations").setViewName("jobConfigurations");
                viewControllerRegistry.addViewController("/jobConfigurationAdd").setViewName("jobConfigurationAdd");
                viewControllerRegistry.addViewController("/jobConfigurationEdit").setViewName("jobConfigurationEdit");
                viewControllerRegistry.addViewController("/jobConfiguration").setViewName("jobConfiguration");
                viewControllerRegistry.addViewController("/jobLaunchers").setViewName("jobLaunchers");
                viewControllerRegistry.addViewController("/jobLauncher").setViewName("jobLauncher");
                viewControllerRegistry.addViewController("/application").setViewName("application");
            }
        };
    }
}