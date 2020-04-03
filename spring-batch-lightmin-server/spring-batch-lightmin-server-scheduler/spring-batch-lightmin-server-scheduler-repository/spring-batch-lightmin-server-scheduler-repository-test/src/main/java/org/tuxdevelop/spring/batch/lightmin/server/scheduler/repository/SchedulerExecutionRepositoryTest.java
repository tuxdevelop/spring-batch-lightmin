package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.After;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.exception.SchedulerExecutionNotFoundException;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;

@Slf4j
public abstract class SchedulerExecutionRepositoryTest extends SchedulerTest {

    @Test
    public void testSave() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("saveExecutionApp");
        this.createSchedulerExecution(schedulerConfiguration.getId());
    }

    @Test
    public void testfindById() throws SchedulerExecutionNotFoundException {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findByIdApp");
        final SchedulerExecution schedulerExecution = this.createSchedulerExecution(schedulerConfiguration.getId());

        final SchedulerExecution result = this.getSchedulerExecutionRepository().findById(schedulerExecution.getId());
        BDDAssertions.then(result).isEqualTo(schedulerExecution);
    }

    @Test
    public void testdeleteById() throws SchedulerExecutionNotFoundException {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("deleteByIdApp");
        final SchedulerExecution schedulerExecution = this.createSchedulerExecution(schedulerConfiguration.getId());

        final SchedulerExecution result = this.getSchedulerExecutionRepository().findById(schedulerExecution.getId());
        BDDAssertions.then(result).isEqualTo(schedulerExecution);

        this.getSchedulerExecutionRepository().delete(result.getId());
        try {
            this.getSchedulerExecutionRepository().findById(result.getId());
            fail("Exception not thrown!");
        } catch (final SchedulerExecutionNotFoundException e) {
            log.debug("Exception caught, every is fine", e);
        }
    }

    @Test
    public void testDeleteBySchedulerConfigurationId() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("deleteBySCIdApp");
        final SchedulerExecution schedulerExecution = this.createSchedulerExecution(schedulerConfiguration.getId());

        this.getSchedulerExecutionRepository().deleteBySchedulerConfigurationId(schedulerConfiguration.getId());
        try {
            this.getSchedulerExecutionRepository().findById(schedulerExecution.getId());
            fail("Exception not thrown!");
        } catch (final SchedulerExecutionNotFoundException e) {
            log.debug("Exception caught, every is fine", e);
        }
    }

    @Test
    public void testFindAll() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findAllApp");
        final SchedulerExecution schedulerExecution = this.createSchedulerExecution(schedulerConfiguration.getId());
        final SchedulerExecution schedulerExecution2 = this.createSchedulerExecution(schedulerConfiguration.getId());

        final List<SchedulerExecution> result = this.getSchedulerExecutionRepository().findAll();

        BDDAssertions.then(result).hasSize(2);
        BDDAssertions.then(result).contains(schedulerExecution);
        BDDAssertions.then(result).contains(schedulerExecution2);
    }

    @Test
    public void testFindAllPaging() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findAllPagingApp");
        for (int i = 0; i < 10; i++) {
            this.createSchedulerExecution(schedulerConfiguration.getId());
        }
        final List<SchedulerExecution> result = this.getSchedulerExecutionRepository().findAll(1, 5);

        BDDAssertions.then(result).hasSize(5);

        final List<SchedulerExecution> result2 = this.getSchedulerExecutionRepository().findAll(5, 5);

        BDDAssertions.then(result2).hasSize(5);

        log.debug("Fetched: {}", result);
        log.debug("Fetched: {}", result2);
    }

    @Test
    public void testFindNextExecutions() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findExeApp");
        final SchedulerExecution schedulerExecution = this.createSchedulerExecution(schedulerConfiguration.getId());
        this.createSchedulerExecution(schedulerConfiguration.getId(), new Date(System.currentTimeMillis() + 1000000));


        final List<SchedulerExecution> result = this.getSchedulerExecutionRepository().findNextExecutions(new Date());

        BDDAssertions.then(result).hasSize(1);
        BDDAssertions.then(result).contains(schedulerExecution);
    }


    @Test
    public void testFindByState() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findStateApp");
        final SchedulerExecution schedulerExecution = this.createSchedulerExecution(schedulerConfiguration.getId(), 1);
        final SchedulerExecution schedulerExecution2 = this.createSchedulerExecution(schedulerConfiguration.getId(), 20);

        final List<SchedulerExecution> result1 = this.getSchedulerExecutionRepository().findByState(1);
        final List<SchedulerExecution> result2 = this.getSchedulerExecutionRepository().findByState(20);

        BDDAssertions.then(result1).hasSize(1);
        BDDAssertions.then(result1).contains(schedulerExecution);
        BDDAssertions.then(result2).hasSize(1);
        BDDAssertions.then(result2).contains(schedulerExecution2);
    }

    @Test
    public void testFindByStatePaging() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findAllPagingApp");
        for (int i = 0; i < 10; i++) {
            this.createSchedulerExecution(schedulerConfiguration.getId(), 1);
        }
        final List<SchedulerExecution> result = this.getSchedulerExecutionRepository().findByState(1, 1, 5);

        BDDAssertions.then(result).hasSize(5);

        final List<SchedulerExecution> result2 = this.getSchedulerExecutionRepository().findByState(1, 5, 5);

        BDDAssertions.then(result2).hasSize(5);

        log.debug("Fetched: {}", result);
        log.debug("Fetched: {}", result2);
    }

    @Test
    public void testFindByStateAndDate() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findStateDateApp");
        final SchedulerExecution schedulerExecution = this.createSchedulerExecution(schedulerConfiguration.getId(), new Date(), 1);
        this.createSchedulerExecution(schedulerConfiguration.getId(), new Date(System.currentTimeMillis() + 100000), 1);
        final SchedulerExecution schedulerExecution2 = this.createSchedulerExecution(schedulerConfiguration.getId(), new Date(), 20);

        final List<SchedulerExecution> result1 = this.getSchedulerExecutionRepository().findByStateAndDate(1, new Date());
        final List<SchedulerExecution> result2 = this.getSchedulerExecutionRepository().findByStateAndDate(20, new Date());

        BDDAssertions.then(result1).hasSize(1);
        BDDAssertions.then(result1).contains(schedulerExecution);
        BDDAssertions.then(result2).hasSize(1);
        BDDAssertions.then(result2).contains(schedulerExecution2);
    }

    @Test
    public void testFindBySchedulerConfigurationId() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findJCIDApp");
        final SchedulerExecution schedulerExecution = this.createSchedulerExecution(schedulerConfiguration.getId(), new Date(), 1);
        final SchedulerExecution schedulerExecution1 = this.createSchedulerExecution(schedulerConfiguration.getId(), new Date(System.currentTimeMillis() + 100000), 1);
        final SchedulerExecution schedulerExecution2 = this.createSchedulerExecution(schedulerConfiguration.getId(), new Date(), 20);

        final List<SchedulerExecution> result = this.getSchedulerExecutionRepository().findBySchedulerConfigurationId(schedulerConfiguration.getId());
        BDDAssertions.then(result).hasSize(3);
        BDDAssertions.then(result).contains(schedulerExecution);
        BDDAssertions.then(result).contains(schedulerExecution1);
        BDDAssertions.then(result).contains(schedulerExecution2);
    }

    @Test
    public void testDeleteByState() {
        final SchedulerConfiguration schedulerConfiguration = this.createSchedulerConfiguration("findDeleteByStateApp");
        final SchedulerExecution schedulerExecution = this.createSchedulerExecution(schedulerConfiguration.getId(), 1);
        this.createSchedulerExecution(schedulerConfiguration.getId(), 20);

        this.getSchedulerExecutionRepository().deleteByState(20);

        final List<SchedulerExecution> result = this.getSchedulerExecutionRepository().findAll();
        BDDAssertions.then(result).hasSize(1);
        BDDAssertions.then(result).contains(schedulerExecution);
    }

    //TODO: write test for count

    @After
    public void cleanUp() {
        getCleanUpRepository().cleanUp();
    }

    protected SchedulerExecution createSchedulerExecution(final Long schedulerConfigurationId, final Date nextExecution, final Integer state) {
        final SchedulerExecution schedulerExecution = new SchedulerExecution();
        schedulerExecution.setExecutionCount(0);
        schedulerExecution.setState(state);
        schedulerExecution.setNextFireTime(nextExecution);
        schedulerExecution.setSchedulerConfigurationId(schedulerConfigurationId);

        final SchedulerExecution result = this.getSchedulerExecutionRepository().save(schedulerExecution);
        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.getId()).isNotNull();

        return result;
    }

    protected SchedulerExecution createSchedulerExecution(final Long schedulerConfigurationId, final Date date) {
        return createSchedulerExecution(schedulerConfigurationId, date, 1);
    }

    protected SchedulerExecution createSchedulerExecution(final Long schedulerConfigurationId, final Integer state) {
        return createSchedulerExecution(schedulerConfigurationId, new Date(), state);
    }

    protected SchedulerExecution createSchedulerExecution(final Long schedulerConfigurationId) {
        return createSchedulerExecution(schedulerConfigurationId, new Date());
    }

    public abstract SchedulerExecutionRepository getSchedulerExecutionRepository();

    @Override
    public abstract CleanUpRepository getCleanUpRepository();
}
