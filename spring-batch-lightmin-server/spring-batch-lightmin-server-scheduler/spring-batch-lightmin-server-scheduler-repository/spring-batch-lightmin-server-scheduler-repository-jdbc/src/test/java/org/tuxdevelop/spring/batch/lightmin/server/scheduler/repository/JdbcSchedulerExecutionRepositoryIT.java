package org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.test.configuration.JdbcTestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JdbcTestConfiguration.class})
public class JdbcSchedulerExecutionRepositoryIT extends SchedulerExecutionRepositoryTest {

    @Autowired
    private SchedulerExecutionRepository schedulerExecutionRepository;
    @Autowired
    private CleanUpRepository cleanUpRepository;
    @Autowired
    private SchedulerConfigurationRepository schedulerConfigurationRepository;

    @Override
    public SchedulerExecutionRepository getSchedulerExecutionRepository() {
        return this.schedulerExecutionRepository;
    }

    @Override
    public SchedulerConfigurationRepository getSchedulerConfigurationRepository() {
        return this.schedulerConfigurationRepository;
    }

    @Override
    public CleanUpRepository getCleanUpRepository() {
        return this.cleanUpRepository;
    }
}
