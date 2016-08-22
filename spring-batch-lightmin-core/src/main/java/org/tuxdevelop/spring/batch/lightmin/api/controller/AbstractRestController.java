package org.tuxdevelop.spring.batch.lightmin.api.controller;

/**
 * @author Marcel Becker
 * @version 0.1
 */
public abstract class AbstractRestController {

    static final String ALL_JOBS = "ALL_JOBS";
    static final String PRODUCES = "application/json";
    static final String CONSUMES = "application/json";

    private static final String ROOT_URI = "/api";

    public final class JobRestControllerAPI {

        private JobRestControllerAPI() {
        }

        public static final String JOB_EXECUTIONS_JOB_EXECUTION_ID = ROOT_URI + "/jobExecutions/{jobexecutionid}";
        public static final String JOB_EXECUTIONS_JOB_INSTANCES_JOB_INSTANCE_ID =
                ROOT_URI + "/jobExecutions/jobInstances/{jobinstanceid}";
        public static final String JOB_INSTANCES_JOB_NAME = ROOT_URI + "/jobInstances/{jobname}";
    }

    public final class JobConfigurationRestControllerAPI {

        private JobConfigurationRestControllerAPI() {
        }

        public static final String JOB_CONFIGURATIONS = ROOT_URI + "/jobconfigurations";
        public static final String JOB_CONFIGURATIONS_JOB_NAME = ROOT_URI + "/jobconfigurations/{jobname}";
        public static final String JOB_CONFIGURATION_JOB_CONFIGURATION_ID = ROOT_URI +
                "/jobconfigurations/jobconfiguration/{jobconfigurationid}";
    }
}
