package org.tuxdevelop.test.configuration;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.tuxdevelop.spring.batch.lightmin.repository.JdbcJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.repository.configuration.JdbcJobConfigurationRepositoryConfigurationProperties;
import org.tuxdevelop.spring.batch.lightmin.repository.test.ITJobConfigurationRepository;

public class ITJdbcJobConfigurationRepository extends JdbcJobConfigurationRepository implements ITJobConfigurationRepository {

    private static final String DELETE_FROM_JOB_PARAMETERS =
            "DELETE FROM %s WHERE id >= 0";
    private static final String DELETE_FROM_JOB_VALUES =
            "DELETE FROM %s WHERE id >= 0";
    private static final String DELETE_FROM_JOB_CONFIGURATION =
            "DELETE FROM %s WHERE job_configuration_id >= 0";


    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final JdbcJobConfigurationRepositoryConfigurationProperties properties;

    public ITJdbcJobConfigurationRepository(final JdbcTemplate jdbcTemplate, final String tablePrefix,
                                            final PlatformTransactionManager platformTransactionManager,
                                            final JdbcJobConfigurationRepositoryConfigurationProperties properties) {
        super(jdbcTemplate, properties);
        this.jdbcTemplate = jdbcTemplate;

        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.properties = properties;
        this.transactionTemplate.setReadOnly(Boolean.FALSE);
    }

    @Override
    public void clean(final String applicationName) {
        this.transactionTemplate.execute(status -> {
            this.jdbcTemplate.update(this.attachTablePrefix(DELETE_FROM_JOB_PARAMETERS,
                    this.properties.getJobConfigurationParameterTableName()));
            this.jdbcTemplate.update(this.attachTablePrefix(DELETE_FROM_JOB_VALUES,
                    this.properties.getJobConfigurationValueTableName()));
            this.jdbcTemplate.update(this.attachTablePrefix(DELETE_FROM_JOB_CONFIGURATION,
                    this.properties.getJobConfigurationTableName()));
            return 1;
        });
    }

    private String attachTablePrefix(final String query, final String tablePrefix) {
        return String.format(query, tablePrefix);
    }
}
