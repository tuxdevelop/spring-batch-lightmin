package org.tuxdevelop.spring.batch.lightmin.api.controller;

/**
 * @author Marcel Becker
 * @version 0.1
 */
public abstract class AbstractRestController {

    public static final String ALL_JOBS = "ALL_JOBS";

    public static final String ROOT_URI = "/api";

    public static final String PRODUCES = "application/json";
    public static final String CONSUMES = "application/json";

    public final class JobRestControllerAPI {

        private JobRestControllerAPI() {
        }

        public static final String JOB_EXECUTIONS_JOB_EXECUTION_ID = ROOT_URI + "/jobExecutions/{jobExecutionId}";
        public static final String JOB_EXECUTIONS_JOB_INSTANCES_JOB_INSTANCE_ID =
                ROOT_URI + "/jobExecutions/jobInstances/{jobInstanceId}";
        public static final String JOB_INSTANCES_JOB_NAME = ROOT_URI + "/jobInstances/{jobName}";
    }

    public final class JobConfigurationRestControllerAPI {

        private JobConfigurationRestControllerAPI() {
        }

        public static final String JOB_CONFIGURATIONS = ROOT_URI + "/jobConfigurations";
        public static final String JOB_CONFIGURATIONS_JOB_NAME = ROOT_URI + "/jobConfigurations/{jobName}";
        public static final String JOB_CONFIGURATION_JOB_CONFIGURATION_ID = ROOT_URI +
                "/jobConfigurations/jobConfiguration/{jobConfigurationId}";
    }
}
