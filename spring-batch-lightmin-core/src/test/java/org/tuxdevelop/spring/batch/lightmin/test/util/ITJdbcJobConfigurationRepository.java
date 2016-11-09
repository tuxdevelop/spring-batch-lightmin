package org.tuxdevelop.spring.batch.lightmin.test.util;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.tuxdevelop.spring.batch.lightmin.admin.repository.JdbcJobConfigurationRepository;
import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;

public class ITJdbcJobConfigurationRepository extends JdbcJobConfigurationRepository implements ITJobConfigurationRepository {

    private static final String DELETE_FROM_JOB_PARAMETERS =
            "DELETE FROM %sJOB_CONFIGURATION_PARAMETERS WHERE id >= 0";
    private static final String DELETE_FROM_JOB_SCHEDULER =
            "DELETE FROM %sJOB_SCHEDULER_CONFIGURATION WHERE id >= 0";
    private static final String DELETE_FROM_JOB_LISTENERS =
            "DELETE FROM %sJOB_LISTENER_CONFIGURATION WHERE id >= 0";
    private static final String DELETE_FROM_JOB_CONFIGURATION =
            "DELETE FROM %sJOB_CONFIGURATION WHERE job_configuration_id >= 0";


    private final JdbcTemplate jdbcTemplate;
    private final String tablePrefix;
    private final TransactionTemplate transactionTemplate;
    private final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

    public ITJdbcJobConfigurationRepository(final JdbcTemplate jdbcTemplate, final String tablePrefix,
                                            final PlatformTransactionManager platformTransactionManager,
                                            final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
        super(jdbcTemplate, tablePrefix, springBatchLightminConfigurationProperties.getConfigurationDatabaseSchema());
        this.jdbcTemplate = jdbcTemplate;
        this.tablePrefix = tablePrefix;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
        transactionTemplate.setReadOnly(Boolean.FALSE);
    }

    public void clean() {
        transactionTemplate.execute(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(final TransactionStatus status) {
                jdbcTemplate.update(attachTablePrefix(DELETE_FROM_JOB_PARAMETERS, tablePrefix));
                jdbcTemplate.update(attachTablePrefix(DELETE_FROM_JOB_SCHEDULER, tablePrefix));
                jdbcTemplate.update(attachTablePrefix(DELETE_FROM_JOB_LISTENERS, tablePrefix));
                jdbcTemplate.update(attachTablePrefix(DELETE_FROM_JOB_CONFIGURATION, tablePrefix));
                return 1;
            }
        });
    }

    private String attachTablePrefix(final String query, final String tablePrefix) {
        return String.format(query, tablePrefix);
    }
}
