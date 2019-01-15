package org.tuxdevelop.spring.batch.lightmin.test.configuration;

import org.springframework.jdbc.core.JdbcTemplate;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.CleanUpRepository;

public class JdbcCleanUpRepository implements CleanUpRepository {

    private static final String DELETE_SCHEDULER_CONFIGURATIONS =
            "DELETE FROM SCHEDULER_CONFIGURATION WHERE id >= 0";

    private static final String DELETE_SCHEDULER_CONFIGURATION_VALUE =
            "DELETE FROM SCHEDULER_CONFIGURATION_VALUE WHERE id >= 0";

    private static final String DELETE_SCHEDULER_EXECUTIONS =
            "DELETE FROM SCHEDULER_EXECUTION WHERE id >= 0";

    private final JdbcTemplate jdbcTemplate;

    public JdbcCleanUpRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void cleanUp() {
        this.jdbcTemplate.update(DELETE_SCHEDULER_EXECUTIONS);
        this.jdbcTemplate.update(DELETE_SCHEDULER_CONFIGURATION_VALUE);
        this.jdbcTemplate.update(DELETE_SCHEDULER_CONFIGURATIONS);
    }
}
