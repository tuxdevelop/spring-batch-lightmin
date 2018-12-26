package org.tuxdevelop.spring.batch.lightmin.client.api.controller;

import org.springframework.http.MediaType;

/**
 * @author Marcel Becker
 * @version 0.1
 */
public abstract class AbstractRestController {

    static final String PRODUCES = MediaType.APPLICATION_JSON_UTF8_VALUE;
    static final String CONSUMES = MediaType.APPLICATION_JSON_UTF8_VALUE;

    private static final String ROOT_URI = "/api";

    public final class JobRestControllerAPI {

        private JobRestControllerAPI() {
        }

        public static final String JOB_EXECUTIONS_JOB_EXECUTION_ID = ROOT_URI + "/jobexecutions/{jobexecutionid}";
        public static final String JOB_EXECUTION_PAGES_INSTANCE_ID = ROOT_URI + "/jobexecutionpages";
        public static final String JOB_EXECUTION_PAGES_INSTANCE_ID_ALL = ROOT_URI + "/jobexecutionpages/all";
        public static final String JOB_INSTANCES_JOB_NAME = ROOT_URI + "/jobinstances";
        public static final String APPLICATION_JOB_INFO = ROOT_URI + "/applicationjobinfos";
        public static final String JOB_INFO_JOB_NAME = ROOT_URI + "/jobinfos/{jobname}";
        public static final String JOB_EXECUTIONS_RESTART = JOB_EXECUTIONS_JOB_EXECUTION_ID + "/restart";
        public static final String JOB_EXECUTIONS_STOP = JOB_EXECUTIONS_JOB_EXECUTION_ID + "/stop";
        public static final String STEP_EXECUTIONS = ROOT_URI + "/stepexecutions/{stepexecutionid}/jobexecutions/{jobexecutionid}";
        public static final String JOB_PARAMETERS = ROOT_URI + "/jobparameters";
        public static final String QUERY_JOB_EXECUTIONS = ROOT_URI + "/jobexecutions/query";
    }

    public final class JobConfigurationRestControllerAPI {

        private JobConfigurationRestControllerAPI() {
        }

        public static final String JOB_CONFIGURATIONS = ROOT_URI + "/jobconfigurations";
        public static final String JOB_CONFIGURATIONS_JOB_NAME = JOB_CONFIGURATIONS + "/{jobname}";
        public static final String JOB_CONFIGURATION_JOB_CONFIGURATION_ID = JOB_CONFIGURATIONS + "/jobconfiguration/{jobconfigurationid}";
        public static final String JOB_CONFIGURATION_START = JOB_CONFIGURATIONS + "/{jobconfigurationid}/start";
        public static final String JOB_CONFIGURATION_STOP = JOB_CONFIGURATIONS + "/{jobconfigurationid}/stop";
    }

    public final class JobLauncherRestControllerAPI {

        private JobLauncherRestControllerAPI() {
        }

        public static final String JOB_LAUNCH = ROOT_URI + "/joblaunches";
    }
}
