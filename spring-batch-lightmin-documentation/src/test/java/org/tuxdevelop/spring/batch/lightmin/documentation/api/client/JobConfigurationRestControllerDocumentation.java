package org.tuxdevelop.spring.batch.lightmin.documentation.api.client;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.client.api.controller.AbstractRestController;
import org.tuxdevelop.spring.batch.lightmin.documentation.api.AbstractServiceDocumentation;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;


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
                .port(this.getServerPort())
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
                .port(this.getServerPort())
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
                .port(this.getServerPort())
                .get(AbstractRestController.JobConfigurationRestControllerAPI.JOB_CONFIGURATION_JOB_CONFIGURATION_ID, this.addedJobConfigurationId)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testAddJobConfiguration() {
        final JobConfiguration jobConfiguration = this.createApiJobConfiguration();
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
                .port(this.getServerPort())
                .body(jobConfiguration)
                .post(AbstractRestController.JobConfigurationRestControllerAPI.JOB_CONFIGURATIONS)
                .then()
                .assertThat().statusCode(is(201));
    }

    @Test
    public void testUpdateJobConfiguration() {
        final JobConfiguration jobConfiguration = this.serviceEntry.getJobConfigurationById(this.addedListenerJobConfigurationId);
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
                .port(this.getServerPort())
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
                .port(this.getServerPort())
                .delete(AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_JOB_CONFIGURATION_ID, this.addedListenerJobConfigurationId)
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
                .port(this.getServerPort())
                .get(AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_START, this.addedListenerJobConfigurationId)
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
                .port(this.getServerPort())
                .get(AbstractRestController.JobConfigurationRestControllerAPI
                        .JOB_CONFIGURATION_STOP, this.addedListenerJobConfigurationId)
                .then()
                .assertThat().statusCode(is(200));

        try {
            Thread.sleep(500L);
        } catch (final InterruptedException e) {
            fail(e.getMessage());
        }
    }
}
