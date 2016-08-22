package org.tuxdevelop.spring.batch.lightmin.admin.repository;


/**
 * @author Marcel Becker
 * @since 0.1
 */
final class JobSchedulerConfigurationDomain {

    private JobSchedulerConfigurationDomain() {
    }

    static final String ID = "id";
    static final String JOB_CONFIGURATION_ID = "job_configuration_id";
    static final String SCHEDULER_TYPE = "scheduler_type";
    static final String CRON_EXPRESSION = "cron_expression";
    static final String INITIAL_DELAY = "initial_delay";
    static final String FIXED_DELAY = "fixed_delay";
    static final String TASK_EXECUTOR_TYPE = "task_executor_type";
    static final String BEAN_NAME = "bean_name";
    static final String STATUS = "status";

}
