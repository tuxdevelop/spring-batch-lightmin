package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.dao.*;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.tuxdevelop.spring.batch.lightmin.dao.JdbcLightminJobExecutionDao;
import org.tuxdevelop.spring.batch.lightmin.service.DefaultJobService;
import org.tuxdevelop.spring.batch.lightmin.service.DefaultStepService;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultSpringBatchLightminConfiguratorTest {

    private DataSource dataSource;

    @Test
    public void initializeJbdcTest() {
        final DefaultSpringBatchLightminConfigurator configurator =
                new DefaultSpringBatchLightminConfigurator(dataSource);
        assertThat(configurator).isNotNull();
        configurator.initialize();
        assertJdbcComponents(configurator);
        assertCommonComponents(configurator);
    }

    @Test
    public void initializeJbdcWithTablePrefixTest() {
        final String tablePrefix = "TEST_BATCH";
        final DefaultSpringBatchLightminConfigurator configurator =
                new DefaultSpringBatchLightminConfigurator(dataSource, tablePrefix);
        assertThat(configurator).isNotNull();
        configurator.initialize();
        assertJdbcComponents(configurator);
        assertCommonComponents(configurator);
    }

    @Test
    public void initializeMapTest() {
        final DefaultSpringBatchLightminConfigurator configurator = new DefaultSpringBatchLightminConfigurator();
        assertThat(configurator).isNotNull();
        configurator.initialize();
        assertMapComponents(configurator);
        assertCommonComponents(configurator);
    }

    @Test
    public void initializeMapWithTablePrefixTest() {
        final String tablePrefix = "TEST_BATCH";
        final DefaultSpringBatchLightminConfigurator configurator =
                new DefaultSpringBatchLightminConfigurator(tablePrefix);
        assertThat(configurator).isNotNull();
        configurator.initialize();
        assertMapComponents(configurator);
        assertCommonComponents(configurator);
    }

    @Before
    public void init() {
        this.dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    private void assertMapComponents(final DefaultSpringBatchLightminConfigurator configurator) {
        assertThat(configurator.getJobExecutionDao()).isInstanceOf(MapJobExecutionDao.class);
        assertThat(configurator.getJobInstanceDao()).isInstanceOf(MapJobInstanceDao.class);
        assertThat(configurator.getStepExecutionDao()).isInstanceOf(MapStepExecutionDao.class);
        assertThat(configurator.getTransactionManager()).isInstanceOf(ResourcelessTransactionManager.class);
        assertThat(configurator.getJobExplorer()).isNotNull();
        assertThat(configurator.getLightminJobExecutionDao()).isNotNull();
    }

    private void assertJdbcComponents(final DefaultSpringBatchLightminConfigurator configurator) {
        assertThat(configurator.getJobExplorer()).isInstanceOf(SimpleJobExplorer.class);
        assertThat(configurator.getJobExecutionDao()).isInstanceOf(JdbcJobExecutionDao.class);
        assertThat(configurator.getLightminJobExecutionDao()).isInstanceOf(JdbcLightminJobExecutionDao.class);
        assertThat(configurator.getJobInstanceDao()).isInstanceOf(JdbcJobInstanceDao.class);
        assertThat(configurator.getStepExecutionDao()).isInstanceOf(JdbcStepExecutionDao.class);
        assertThat(configurator.getTransactionManager()).isInstanceOf(DataSourceTransactionManager.class);
    }

    private void assertCommonComponents(final DefaultSpringBatchLightminConfigurator configurator) {
        assertThat(configurator.getJobRegistry()).isInstanceOf(MapJobRegistry.class);
        assertThat(configurator.getJobLauncher()).isInstanceOf(SimpleJobLauncher.class);
        assertThat(configurator.getJobOperator()).isInstanceOf(SimpleJobOperator.class);
        assertThat(configurator.getStepService()).isInstanceOf(DefaultStepService.class);
        assertThat(configurator.getJobService()).isInstanceOf(DefaultJobService.class);
        assertThat(configurator.getJobRepository()).isNotNull();
    }

}
