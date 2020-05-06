package org.tuxdevelop.spring.batch.lightmin.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.CleanUpRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepository;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.SchedulerExecutionRepositoryTest;
import org.tuxdevelop.spring.batch.lightmin.test.configuration.InfinispanITConfiguration;

import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {InfinispanITConfiguration.class})
public class InfinispanSchedulerExecutionRepositoryIT extends SchedulerExecutionRepositoryTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CleanUpRepository cleanUpRepository;

    @Autowired
    private SchedulerExecutionRepository schedulerExecutionRepository;

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

    @Before
    public void init() {
        final Map<String, SchedulerConfigurationRepository> beans =
                this.applicationContext.getBeansOfType(SchedulerConfigurationRepository.class);
        beans.forEach((key, value) -> log.info("name: {} - bean {}", key, value));
        final Map<String, SchedulerExecutionRepository> execBeans =
                this.applicationContext.getBeansOfType(SchedulerExecutionRepository.class);
        execBeans.forEach((key, value) -> log.info("name: {} - bean {}", key, value));
    }
}
