package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.tuxdevelop.spring.batch.lightmin.ITPersistenceConfiguration;
import org.tuxdevelop.spring.batch.lightmin.dao.JdbcLightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.service.DefaultJobService;
import org.tuxdevelop.spring.batch.lightmin.service.DefaultStepService;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultSpringBatchLightminConfiguratorTest {

    private DataSource dataSource;
    private SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;
    private ApplicationContext applicationContext;

    @Test
    public void initializeJbdcWithTablePrefixTest() {
        final String tablePrefix = "TEST_BATCH";
        springBatchLightminConfigurationProperties.setRepositoryTablePrefix(tablePrefix);
        final DefaultSpringBatchLightminConfigurator configurator =
                new DefaultSpringBatchLightminConfigurator(springBatchLightminConfigurationProperties, applicationContext);
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer(dataSource, tablePrefix);
        batchConfigurer.initialize();
        assertThat(configurator).isNotNull();
        configurator.setBatchConfigurer(batchConfigurer);
        configurator.initialize();
        assertJdbcComponents(configurator);
        assertCommonComponents(configurator);
        ((ConfigurableApplicationContext) applicationContext).close();
    }

    @Test
    public void initializeMapTest() {
        springBatchLightminConfigurationProperties.setBatchRepositoryType(BatchRepositoryType.MAP);
        springBatchLightminConfigurationProperties.setLightminRepositoryType(LightminRepositoryType.MAP);
        final DefaultSpringBatchLightminConfigurator configurator = new DefaultSpringBatchLightminConfigurator(springBatchLightminConfigurationProperties, applicationContext);
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer();
        batchConfigurer.initialize();
        assertThat(configurator).isNotNull();
        configurator.setBatchConfigurer(batchConfigurer);
        configurator.initialize();
        assertMapComponents(configurator);
        assertCommonComponents(configurator);
        ((ConfigurableApplicationContext) applicationContext).close();
    }

    @Before
    public void init() {
        this.springBatchLightminConfigurationProperties = new SpringBatchLightminConfigurationProperties();
        this.applicationContext = new AnnotationConfigApplicationContext(ITPersistenceConfiguration.class);
        this.dataSource = applicationContext.getBean("dataSource", DataSource.class);
    }

    private void assertMapComponents(final DefaultSpringBatchLightminConfigurator configurator) {
        assertThat(configurator.getLightminJobExecutionDao()).isNotNull();
    }

    private void assertJdbcComponents(final DefaultSpringBatchLightminConfigurator configurator) {
        assertThat(configurator.getLightminJobExecutionDao()).isInstanceOf(JdbcLightminJobExecutionDao.class);
    }

    private void assertCommonComponents(final DefaultSpringBatchLightminConfigurator configurator) {
        assertThat(configurator.getJobRegistry()).isInstanceOf(MapJobRegistry.class);
        assertThat(configurator.getJobOperator()).isInstanceOf(SimpleJobOperator.class);
        assertThat(configurator.getStepService()).isInstanceOf(DefaultStepService.class);
        assertThat(configurator.getJobService()).isInstanceOf(DefaultJobService.class);
    }

}
