package org.tuxdevelop.spring.batch.lightmin;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;

@Configuration
@Import(value = {ITPersistenceConfiguration.class})
@PropertySource(value = "classpath:application.properties")
public class ITInfrastructureConfiguration {

    @Bean
    public StepBuilderFactory stepBuilderFactory(final JobRepository jobRepository) {
        return new StepBuilderFactory(jobRepository, new ResourcelessTransactionManager());
    }

    @Bean
    public JobBuilderFactory jobBuilderFactory(final JobRepository jobRepository) {
        return new JobBuilderFactory(jobRepository);
    }

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        final PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(new ClassPathResource
                ("application.properties"));
        return propertyPlaceholderConfigurer;
    }

}
