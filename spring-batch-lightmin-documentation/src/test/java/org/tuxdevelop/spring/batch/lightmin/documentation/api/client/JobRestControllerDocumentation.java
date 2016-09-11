package org.tuxdevelop.spring.batch.lightmin.documentation.api.client;


import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.api.controller.AbstractRestController;
import org.tuxdevelop.spring.batch.lightmin.documentation.api.AbstractServiceDocumentation;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

public class JobRestControllerDocumentation extends AbstractServiceDocumentation {


    @Test
    public void testGetJobExecutionById() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jobexecutionid").description("The job execution id"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_EXECUTIONS_JOB_EXECUTION_ID, launchedJobExecutionId)
                .then()
                .assertThat().statusCode(is(200));

    }

    @Test
    public void testGetJobExecutionsByJobInstanceId() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("jobinstanceid").description("The job instance id"),
                                parameterWithName("startindex").description("Index start position of the page"),
                                parameterWithName("pagesize").description("Size of the page"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID
                        + "?jobinstanceid=" + launchedJobInstanceId
                        + "&startindex=0"
                        + "&pagesize=5")
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testGetAllJobExecutionsByJobInstanceId() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("jobinstanceid").description("The job instance id"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID_ALL
                        + "?jobinstanceid=" + launchedJobInstanceId)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testGetJobInstancesByJobName() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("jobname").description("The name of the Spring Batch Job"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_INSTANCES_JOB_NAME
                        + "?jobname=simpleJob")
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testGetApplicationJobInfo() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.APPLICATION_JOB_INFO)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testGetJobInfo() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
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
                .get(AbstractRestController.JobRestControllerAPI.JOB_INFO_JOB_NAME, "simpleJob")
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testRestartJobExecution() {
        launchSimpleJobWithOutParameters();
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jobexecutionid").description("The id of the Job Execution"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_EXECUTIONS_RESTART, launchedJobExecutionId)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testStopJobExecution() {
        launchSimpleBlockingJob();
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("jobexecutionid").description("The id of the Job Execution"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_EXECUTIONS_STOP, launchedJobExecutionId)
                .then()
                .assertThat().statusCode(is(200));
        myThread.stop();
    }

    @Test
    public void testGetStepExecution() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("stepexecutionid").description("The id of the Step Execution"),
                                parameterWithName("jobexecutionid").description("The id of the Job Execution the " +
                                        "Step Excution belongs to"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.STEP_EXECUTIONS, launchedStepExecutionId, launchedJobExecutionId)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testGetLastJobParameters() {
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("jobname").description("The name of the Spring Batch Job"))))
                .when()
                .port(getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_PARAMETERS + "?jobname=simpleJob")
                .then()
                .assertThat().statusCode(is(200));
    }
}


