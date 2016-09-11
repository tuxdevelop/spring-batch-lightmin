package org.tuxdevelop.spring.batch.lightmin.documentation.api.client;

import com.jayway.restassured.http.ContentType;
import org.junit.Ignore;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.api.controller.AbstractRestController;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.documentation.api.AbstractServiceDocumentation;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;


public class JobConfigurationRestControllerDocumentation extends AbstractServiceDocumentation {

    @Test
    public void testGetJobConfigurations() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobconfigurationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testGetJobConfigurationsByJobName() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobconfigurationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jobname").description("The name of the Spring Batch Job"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS_JOB_NAME, "simpleJob")
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testGetJobConfigurationById() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobconfigurationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jobconfigurationid").description("The id of the Job " +
                                        "Configuration"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID, addedJobConfigurationId)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    @Ignore
    public void testAddJobConfiguration() {
        final JobConfiguration jobConfiguration = createApiJobConfiguration();
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .filter(document("jobconfigurationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .when()
                .port(getServerPort())
                .body(jobConfiguration)
                .post(AbstractRestController.JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS)
                .then()
                .assertThat().statusCode(is(201));
    }

    @Test
    public void testUpdateJobConfiguration() {
        final JobConfiguration jobConfiguration = serviceEntry.getJobConfigurationById(addedListenerJobConfigurationId);
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .filter(document("jobconfigurationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .when()
                .port(getServerPort())
                .body(jobConfiguration)
                .put(AbstractRestController.JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testdeleteJobConfigurationById() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobconfigurationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jobconfigurationid").description("The id of the Job " +
                                        "Configuration"))))
                .when()
                .port(getServerPort())
                .delete(AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_JOB_CONFIGURATION_ID, addedListenerJobConfigurationId)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testStartJobConfiguration() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobconfigurationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jobconfigurationid").description("The id of the Job " +
                                        "Configuration"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_START, addedListenerJobConfigurationId)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testStopJobConfiguration() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobconfigurationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jobconfigurationid").description("The id of the Job " +
                                        "Configuration"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_STOP, addedListenerJobConfigurationId)
                .then()
                .assertThat().statusCode(is(200));
    }
}
