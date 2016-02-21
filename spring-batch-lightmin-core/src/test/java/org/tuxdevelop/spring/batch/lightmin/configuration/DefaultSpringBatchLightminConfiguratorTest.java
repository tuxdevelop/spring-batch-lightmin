package org.tuxdevelop.spring.batch.lightmin.configuration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
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
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer(dataSource);
        batchConfigurer.initialize();
        configurator.setBatchConfigurer(batchConfigurer);
        assertThat(configurator).isNotNull();
        configurator.setBatchConfigurer(batchConfigurer);
        configurator.initialize();
        assertJdbcComponents(configurator);
        assertCommonComponents(configurator);
    }

    @Test
    public void initializeJbdcWithTablePrefixTest() {
        final String tablePrefix = "TEST_BATCH";
        final DefaultSpringBatchLightminConfigurator configurator =
                new DefaultSpringBatchLightminConfigurator(dataSource, tablePrefix);
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer = new
                DefaultSpringBatchLightminBatchConfigurer(dataSource, tablePrefix);
        batchConfigurer.initialize();
        assertThat(configurator).isNotNull();
        configurator.setBatchConfigurer(batchConfigurer);
        configurator.initialize();
        assertJdbcComponents(configurator);
        assertCommonComponents(configurator);
    }

    @Test
    public void initializeMapTest() {
        final DefaultSpringBatchLightminConfigurator configurator = new DefaultSpringBatchLightminConfigurator();
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer = new DefaultSpringBatchLightminBatchConfigurer();
        batchConfigurer.initialize();
        assertThat(configurator).isNotNull();
        configurator.setBatchConfigurer(batchConfigurer);
        configurator.initialize();
        assertMapComponents(configurator);
        assertCommonComponents(configurator);
    }

    @Test
    public void initializeMapWithTablePrefixTest() {
        final String tablePrefix = "TEST_BATCH";
        final DefaultSpringBatchLightminConfigurator configurator =
                new DefaultSpringBatchLightminConfigurator(tablePrefix);
        final DefaultSpringBatchLightminBatchConfigurer batchConfigurer = new
                DefaultSpringBatchLightminBatchConfigurer(tablePrefix);
        batchConfigurer.initialize();
        assertThat(configurator).isNotNull();
        configurator.setBatchConfigurer(batchConfigurer);
        configurator.initialize();
        assertMapComponents(configurator);
        assertCommonComponents(configurator);
    }

    @Before
    public void init() {
        this.dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
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
