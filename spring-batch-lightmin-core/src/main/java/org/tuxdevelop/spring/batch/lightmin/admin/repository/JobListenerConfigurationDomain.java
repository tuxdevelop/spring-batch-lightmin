package org.tuxdevelop.spring.batch.lightmin.admin.repository;

/**
 * @author Marcel Becker
 * @since 0.3
 */
public final class JobListenerConfigurationDomain {

    private JobListenerConfigurationDomain() {
    }

    static final String ID = "id";
    static final String JOB_CONFIGURATION_ID = "job_configuration_id";
    static final String LISTENER_TYPE = "listener_type";
    static final String SOURCE_FOLDER = "source_folder";
    static final String FILE_PATTERN = "file_pattern";
    static final String POLLER_PERIOD = "poller_period";
    static final String BEAN_NAME = "bean_name";
    static final String TASK_EXECUTOR_TYPE = "task_executor_type";
    static final String STATUS = "status";
}
