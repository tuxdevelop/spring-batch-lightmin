package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.After;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;

import java.util.List;

@Slf4j
public abstract class SchedulerConfigurationRepositoryTest extends SchedulerTest {

    @Test
    public void testSaveSchedulerConfiguration() {
        createSchedulerConfiguration("saveApp");
    }

    @Test
    public void testSaveUpdateSchedulerConfiguration() {
        final SchedulerConfiguration schedulerConfiguration = createSchedulerConfiguration("saveApp");
        schedulerConfiguration.setCronExpression("changed");
        final SchedulerConfiguration updated = this.getSchedulerConfigurationRepository().save(schedulerConfiguration);
        BDDAssertions.then(updated.getId()).isEqualTo(schedulerConfiguration.getId());
        BDDAssertions.then(updated.getCronExpression()).isEqualTo("changed");
    }

    @Test
    public void testFindAll() {
        final SchedulerConfiguration schedulerConfiguration = createSchedulerConfiguration("findAllApp");

        final List<SchedulerConfiguration> result = this.getSchedulerConfigurationRepository().findAll();
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result).isNotEmpty();
        BDDAssertions.then(result).contains(schedulerConfiguration);
        log.info("fetched: {}", result);
    }

    @Test
    public void testFindById() {
        final SchedulerConfiguration schedulerConfiguration = createSchedulerConfiguration("findByIdApp");
        final SchedulerConfiguration result = getSchedulerConfigurationRepository().findById(schedulerConfiguration.getId());
        BDDAssertions.then(result).isEqualTo(schedulerConfiguration);
    }

    @Test
    public void testFindByApplication() {
        final SchedulerConfiguration schedulerConfiguration = createSchedulerConfiguration("findByApplicationApp");
        final SchedulerConfiguration schedulerConfiguration2 = createSchedulerConfiguration("findByApplicationApp");

        final List<SchedulerConfiguration> result = getSchedulerConfigurationRepository().findByApplication("findByApplicationApp");
        BDDAssertions.then(result).hasSize(2);
        BDDAssertions.then(result).contains(schedulerConfiguration);
        BDDAssertions.then(result).contains(schedulerConfiguration2);

    }

    @Test
    public void testDeleteById() {
        final SchedulerConfiguration schedulerConfiguration = createSchedulerConfiguration("deleteIdApp");
        final SchedulerConfiguration schedulerConfiguration2 = createSchedulerConfiguration("deleteIdApp");

        final List<SchedulerConfiguration> all = this.getSchedulerConfigurationRepository().findAll();
        BDDAssertions.then(all).hasSize(2);

        this.getSchedulerConfigurationRepository().delete(schedulerConfiguration.getId());

        final List<SchedulerConfiguration> allAfterDeletion = this.getSchedulerConfigurationRepository().findAll();
        BDDAssertions.then(allAfterDeletion).hasSize(1);
        BDDAssertions.then(allAfterDeletion).contains(schedulerConfiguration2);

    }

    @After
    public void cleanUp() {
        getCleanUpRepository().cleanUp();
    }

}
