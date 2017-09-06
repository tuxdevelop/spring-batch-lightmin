package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.tuxdevelop.spring.batch.lightmin.dao.JdbcLightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.service.DefaultJobService;
import org.tuxdevelop.spring.batch.lightmin.service.DefaultStepService;
import org.tuxdevelop.test.configuration.ITConfiguration;
import org.tuxdevelop.test.configuration.ITPersistenceConfiguration;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultSpringBatchLightminConfiguratorTest {

    private DataSource dataSource;
    private SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;
    private ApplicationContext applicationContext;

    @Test
    public void initializeJbdcWithTablePrefixTest() {
        this.applicationContext = new AnnotationConfigApplicationContext(ITPersistenceConfiguration.class);
        this.dataSource = this.applicationContext.getBean("dataSource", DataSource.class);
        final String tablePrefix = "TEST_BATCH";
        this.springBatchLightminConfigurationProperties.setRepositoryTablePrefix(tablePrefix);
        final DefaultSpringBatchLightminConfigurator configurator =
                new DefaultSpringBatchLightminConfigurator(this.springBatchLightminConfigurationProperties, this.applicationContext);
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer(this.dataSource, tablePrefix);
        batchConfigurer.initialize();
        assertThat(configurator).isNotNull();
        configurator.setBatchConfigurer(batchConfigurer);
        configurator.initialize();
        assertJdbcComponents(configurator);
        assertCommonComponents(configurator);
        ((ConfigurableApplicationContext) this.applicationContext).close();
    }

    @Test
    public void initializeMapTest() {
        this.applicationContext = new AnnotationConfigApplicationContext(ITConfiguration.class);
        this.springBatchLightminConfigurationProperties.setBatchRepositoryType(BatchRepositoryType.MAP);
        this.springBatchLightminConfigurationProperties.setLightminRepositoryType(LightminRepositoryType.MAP);
        final DefaultSpringBatchLightminConfigurator configurator = new DefaultSpringBatchLightminConfigurator(this.springBatchLightminConfigurationProperties, this.applicationContext);
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer();
        batchConfigurer.initialize();
        assertThat(configurator).isNotNull();
        configurator.setBatchConfigurer(batchConfigurer);
        configurator.initialize();
        assertMapComponents(configurator);
        assertCommonComponents(configurator);
        ((ConfigurableApplicationContext) this.applicationContext).close();
    }

    @Before
    public void init() {
        final Environment environment = new MockEnvironment();
        this.springBatchLightminConfigurationProperties = new SpringBatchLightminConfigurationProperties(environment);
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
