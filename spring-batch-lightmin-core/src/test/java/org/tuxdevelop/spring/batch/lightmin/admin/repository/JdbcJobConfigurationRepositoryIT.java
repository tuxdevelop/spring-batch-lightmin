package org.tuxdevelop.spring.batch.lightmin.admin.repository;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.tuxdevelop.spring.batch.lightmin.test.util.ITJdbcJobConfigurationRepository;
import org.tuxdevelop.test.configuration.ITPersistenceConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ITPersistenceConfiguration.class)
public class JdbcJobConfigurationRepositoryIT extends JobConfigurationRepositoryIT {

    @Autowired
    private ITJdbcJobConfigurationRepository itJdbcJobConfigurationRepository;
    @Autowired
    private JobConfigurationRepository jobConfigurationRepository;

    @Override
    JobConfigurationRepository getJobConfigurationRepository() {
        return this.jobConfigurationRepository;
    }

    @Override
    ITJdbcJobConfigurationRepository getITItJdbcJobConfigurationRepository() {
        return this.itJdbcJobConfigurationRepository;
    }
}
