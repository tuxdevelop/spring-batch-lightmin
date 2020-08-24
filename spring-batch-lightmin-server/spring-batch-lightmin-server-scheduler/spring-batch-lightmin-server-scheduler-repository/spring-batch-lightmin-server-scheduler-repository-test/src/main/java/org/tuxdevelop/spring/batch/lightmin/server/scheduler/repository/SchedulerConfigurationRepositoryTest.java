package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.After;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerConfigurationNotFoundException;

import java.util.List;

@Slf4j
public abstract class SchedulerConfigurationRepositoryTest extends SchedulerTest {

    @Test
    public void testSaveSchedulerConfiguration() {
        this.createSchedulerConfiguration("saveApp");
    }

    @Test
    public void testSaveUpdateSchedulerConfiguration() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("saveApp");
        schedulerConfiguration.setCronExpression("changed");
        final SchedulerConfiguration updated = this.getSchedulerConfigurationRepository().save(schedulerConfiguration);
        BDDAssertions.then(updated.getId()).isEqualTo(schedulerConfiguration.getId());
        BDDAssertions.then(updated.getCronExpression()).isEqualTo("changed");
    }

    @Test
    public void testSaveUpdateSchedulerWithStatusChangeConfiguration() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("saveApp");
        final SchedulerConfiguration schedulerConfiguration2 = this.createSchedulerConfiguration("saveApp");
        schedulerConfiguration.setStatus(ServerSchedulerStatus.STOPPED);

        final SchedulerConfiguration updated = this.getSchedulerConfigurationRepository().save(schedulerConfiguration);
        BDDAssertions.then(updated.getStatus()).isEqualTo(ServerSchedulerStatus.STOPPED);
        try {
            BDDAssertions.then(schedulerConfiguration2.getStatus()).isEqualTo(this.getSchedulerConfigurationRepository().findById(schedulerConfiguration2.getId()).getStatus());
        } catch (SchedulerConfigurationNotFoundException e) {
            BDDAssertions.fail(e.getMessage());
        }
    }

    @Test
    public void testSaveUpdateSchedulerConfigurationWithOutRetry() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("saveApp", Boolean.FALSE);
        schedulerConfiguration.setCronExpression("changed");
        final SchedulerConfiguration updated = this.getSchedulerConfigurationRepository().save(schedulerConfiguration);
        BDDAssertions.then(updated.getId()).isEqualTo(schedulerConfiguration.getId());
        BDDAssertions.then(updated.getCronExpression()).isEqualTo("changed");
    }

    @Test
    public void testFindAll() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findAllApp");

        final List<SchedulerConfiguration> result = this.getSchedulerConfigurationRepository().findAll();
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result).isNotEmpty();
        BDDAssertions.then(result).contains(schedulerConfiguration);
        log.info("fetched: {}", result);
    }

    @Test
    public void testFindAllPaging() {
        for (int i = 0; i < 10; i++) {
            this.createSchedulerConfiguration("findAllPagingApp");
        }

        final List<SchedulerConfiguration> result = this.getSchedulerConfigurationRepository().findAll(0, 5);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result).isNotEmpty();
        BDDAssertions.then(result).hasSize(5);
        log.info("fetched: {}", result);

        final List<SchedulerConfiguration> result2 = this.getSchedulerConfigurationRepository().findAll(5, 5);
        BDDAssertions.then(result2).isNotNull();
        BDDAssertions.then(result2).isNotEmpty();
        BDDAssertions.then(result2).hasSize(5);
        log.info("fetched: {}", result2);
    }

    @Test
    public void testGetCount() {
        for (int i = 0; i < 10; i++) {
            this.createSchedulerConfiguration("countApp");
        }
        final List<SchedulerConfiguration> result = this.getSchedulerConfigurationRepository().findAll();
        final Integer count = this.getSchedulerConfigurationRepository().getCount();

        BDDAssertions.then(count).isEqualTo(result.size());
    }

    @Test
    public void testFindById() throws SchedulerConfigurationNotFoundException {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findByIdApp");
        final SchedulerConfiguration result = this.getSchedulerConfigurationRepository().findById(schedulerConfiguration.getId());
        BDDAssertions.then(result).isEqualTo(schedulerConfiguration);
    }

    @Test
    public void testFindByApplication() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findByApplicationApp");
        final SchedulerConfiguration schedulerConfiguration2 = this.createSchedulerConfiguration("findByApplicationApp");

        final List<SchedulerConfiguration> result = this.getSchedulerConfigurationRepository().findByApplication("findByApplicationApp");
        BDDAssertions.then(result).hasSize(2);
        BDDAssertions.then(result).contains(schedulerConfiguration);
        BDDAssertions.then(result).contains(schedulerConfiguration2);

    }

    @Test
    public void testDeleteById() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("deleteIdApp");
        final SchedulerConfiguration schedulerConfiguration2 = this.createSchedulerConfiguration("deleteIdApp");

        final List<SchedulerConfiguration> all = this.getSchedulerConfigurationRepository().findAll();
        BDDAssertions.then(all).hasSize(2);

        this.getSchedulerConfigurationRepository().delete(schedulerConfiguration.getId());

        final List<SchedulerConfiguration> allAfterDeletion = this.getSchedulerConfigurationRepository().findAll();
        BDDAssertions.then(allAfterDeletion).hasSize(1);
        BDDAssertions.then(allAfterDeletion).contains(schedulerConfiguration2);

    }

    @After
    public void cleanUp() {
        this.getCleanUpRepository().cleanUp();
    }

}
