package org.tuxdevelop.spring.batch.lightmin.admin.repository;


public final class JobSchedulerConfigurationDomain {

    private JobSchedulerConfigurationDomain(){}

    public static final String ID = "id";
    public static final String JOB_CONFIGURATION_ID = "job_configuration_id";
    public static final String SCHEDULER_TYPE = "scheduler_type";
    public static final String CRON_EXPRESSION = "cron_expression";
    public static final String INITIAL_DELAY = "initial_delay";
    public static final String FIXED_DELAY = "fixed_delay";
    public static final String TASK_EXECUTOR_TYPE = "task_executor_type";
    public static final String BEAN_NAME = "bean_name";
    public static final String STATUS = "status";

}
