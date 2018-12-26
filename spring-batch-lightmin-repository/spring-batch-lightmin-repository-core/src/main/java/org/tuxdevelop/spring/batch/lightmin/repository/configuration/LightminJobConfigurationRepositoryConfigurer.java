package org.tuxdevelop.spring.batch.lightmin.repository.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.repository.JobConfigurationRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
public class LightminJobConfigurationRepositoryConfigurer implements ApplicationContextAware {

    private JobConfigurationRepository jobConfigurationRepository;
    private ApplicationContext applicationContext;

    protected void configureJobConfigurationRepository() {

    }

    @Bean
    public JobConfigurationRepository jobConfigurationRepository() {
        return this.getJobConfigurationRepository();
    }

    public void setJobConfigurationRepository(final JobConfigurationRepository jobConfigurationRepository) {
        this.jobConfigurationRepository = jobConfigurationRepository;
    }

    protected JobConfigurationRepository getJobConfigurationRepository() {
        final JobConfigurationRepository bean;
        if (this.jobConfigurationRepository != null) {
            bean = this.jobConfigurationRepository;
        } else {
            log.info("No JobConfigurationRepository configured, try to find one ..");
            final Map<String, JobConfigurationRepository> jobConfigurationRepositories
                    = this.applicationContext.getBeansOfType(JobConfigurationRepository.class);

            if (jobConfigurationRepositories.isEmpty()) {
                throw new SpringBatchLightminConfigurationException(
                        "At least one JobConfigurationRepository has to be configured");
            } else {
                if (jobConfigurationRepositories.size() > 1) {
                    final Set<String> beanNames = jobConfigurationRepositories.keySet();
                    final Optional<JobConfigurationRepository> primaryBean = this.findSinglePrimary(beanNames);
                    if (primaryBean.isPresent()) {
                        bean = primaryBean.get();
                    } else {
                        throw new SpringBatchLightminConfigurationException(
                                "Multiple beans of JobConfigurationRepository found: " + beanNames);
                    }
                } else {
                    final Optional<JobConfigurationRepository> firstEntry = jobConfigurationRepositories.values()
                            .stream()
                            .findFirst();
                    bean = firstEntry.orElseThrow(
                            () -> new SpringBatchLightminConfigurationException(
                                    "Required JobConfigurationRepository not present"));
                }
            }
        }
        return bean;
    }

    private Optional<JobConfigurationRepository> findSinglePrimary(final Set<String> jobConfigurationRepositoryBeans) {
        final Optional<JobConfigurationRepository> jobConfigurationRepository;
        final AutowireCapableBeanFactory factory = this.applicationContext.getAutowireCapableBeanFactory();
        final ConfigurableListableBeanFactory clbf = (ConfigurableListableBeanFactory) factory;
        final List<JobConfigurationRepository> primaryRepositories = new ArrayList<>();
        for (final String jobConfigurationRepositoryBean : jobConfigurationRepositoryBeans) {
            final BeanDefinition beanDefinition = clbf.getBeanDefinition(jobConfigurationRepositoryBean);
            if (beanDefinition.isPrimary()) {
                final JobConfigurationRepository repository = this.applicationContext.getBean(
                        jobConfigurationRepositoryBean, JobConfigurationRepository.class);
                primaryRepositories.add(repository);
            } else {
                log.trace("No primary JobConfigurationRepository: {}", jobConfigurationRepositoryBean);
            }
        }
        if (primaryRepositories.isEmpty()) {
            jobConfigurationRepository = Optional.empty();
            log.debug("No primary JobConfigurationRepository found");
        } else {
            if (primaryRepositories.size() > 1) {
                jobConfigurationRepository = Optional.empty();
                log.warn("Multiple primary JobConfigurationRepository found ! Can not provide a unique bean.");
            } else {
                final Optional<JobConfigurationRepository> firstEntry = primaryRepositories
                        .stream()
                        .findFirst();
                jobConfigurationRepository = Optional.of(firstEntry.orElseThrow(
                        () -> new SpringBatchLightminConfigurationException(
                                "Required JobConfigurationRepository not present")));
            }
        }
        return jobConfigurationRepository;
    }

    @PostConstruct
    protected void init() {
        this.configureJobConfigurationRepository();
    }

    protected ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
