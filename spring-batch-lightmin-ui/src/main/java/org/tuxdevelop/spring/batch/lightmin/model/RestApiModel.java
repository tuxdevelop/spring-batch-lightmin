package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tuxdevelop.spring.batch.lightmin.api.controller.AbstractRestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.1
 */
public class RestApiModel {

    private static final String JOB_URIS = "Jobs URIs";
    private static final String JOB_CONFIGURATION_URIS = "JobConfigurations URIs";

    @Getter
    private Map<String, List<MethodUriModel>> apiMap;

    public RestApiModel() {
        apiMap = new HashMap<String, List<MethodUriModel>>();
        final List<MethodUriModel> jobUris = new LinkedList<MethodUriModel>();
        jobUris.add(new MethodUriModel(RequestMethod.GET.name(), AbstractRestController.JobRestControllerAPI
                .JOB_EXECUTIONS_JOB_EXECUTION_ID, UriDescription.JOB_EXECUTIONS_JOB_EXECUTION_ID_GET_DESC));
        jobUris.add(new MethodUriModel(RequestMethod.GET.name(), AbstractRestController.JobRestControllerAPI
                .JOB_EXECUTIONS_JOB_INSTANCES_JOB_INSTANCE_ID, UriDescription.JOB_EXECUTIONS_JOB_INSTANCE_ID_GET_DESC));
        jobUris.add(new MethodUriModel(RequestMethod.GET.name(), AbstractRestController.JobRestControllerAPI
                .JOB_INSTANCES_JOB_NAME, UriDescription.JOB_INSTANCES_JOB_NAME_GET_DESC));

        final List<MethodUriModel> jobConfigurationUris = new LinkedList<MethodUriModel>();
        jobConfigurationUris.add(new MethodUriModel(RequestMethod.GET.name(), AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, UriDescription.JOB_CONFIGURATIONS_GET_DESC));
        jobConfigurationUris.add(new MethodUriModel(RequestMethod.GET.name(), AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID, UriDescription.JOB_CONFIGURATIONS_JOB_CONFIGURATION_ID_GET_DESC));
        jobConfigurationUris.add(new MethodUriModel(RequestMethod.GET.name(), AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME, UriDescription.JOB_CONFIGURATIONS_JOB_NAME_GET_DESC));
        jobConfigurationUris.add(new MethodUriModel(RequestMethod.POST.name(), AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, UriDescription.JOB_CONFIGURATIONS_POST_DESC));
        jobConfigurationUris.add(new MethodUriModel(RequestMethod.PUT.name(), AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS, UriDescription.JOB_CONFIGURATIONS_PUT_DESC));
        jobConfigurationUris.add(new MethodUriModel(RequestMethod.DELETE.name(), AbstractRestController
                .JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID, UriDescription.JOB_CONFIGURATION_JOB_CONFIGURATION_ID_DELETE_DESC));

        apiMap.put(JOB_URIS, jobUris);
        apiMap.put(JOB_CONFIGURATION_URIS, jobConfigurationUris);
    }

    public class MethodUriModel {
        @Getter
        private String method;
        @Getter
        private String uri;
        @Getter
        private String description;

        public MethodUriModel(final String method, final String uri, final String description) {
            this.method = method;
            this.uri = uri;
            this.description = description;
        }
    }

    private abstract class UriDescription {
        //Job
        public static final String JOB_EXECUTIONS_JOB_EXECUTION_ID_GET_DESC = "Get a JobExecution by id";
        public static final String JOB_EXECUTIONS_JOB_INSTANCE_ID_GET_DESC = "Get all JobExecutions by jobInstanceId";
        public static final String JOB_INSTANCES_JOB_NAME_GET_DESC = "Get all JobInstances by job name";

        //JobConfiguration
        public static final String JOB_CONFIGURATIONS_GET_DESC = "Get all JobConfigurations";
        public static final String JOB_CONFIGURATIONS_POST_DESC = "Add a new JobConfiguration to a job";
        public static final String JOB_CONFIGURATIONS_PUT_DESC = "Delete a JobConfiguration";
        public static final String JOB_CONFIGURATIONS_JOB_CONFIGURATION_ID_GET_DESC = "Get a JobConfigurations by id";
        public static final String JOB_CONFIGURATIONS_JOB_NAME_GET_DESC = "Get all JobConfigurations a job by name";
        public static final String JOB_CONFIGURATION_JOB_CONFIGURATION_ID_DELETE_DESC = "Delete a JobConfiguration";


    }

}
