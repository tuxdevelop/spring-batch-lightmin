package org.tuxdevelop.spring.batch.lightmin.documentation.api.client;


import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.batch.dao.QueryParameterKey;
import org.tuxdevelop.spring.batch.lightmin.client.api.controller.AbstractRestController;
import org.tuxdevelop.spring.batch.lightmin.documentation.api.AbstractServiceDocumentation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

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
                .port(this.getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_EXECUTIONS_JOB_EXECUTION_ID, this.launchedJobExecutionId)
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
                .port(this.getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID
                        + "?jobinstanceid=" + this.launchedJobInstanceId
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
                .port(this.getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_EXECUTION_PAGES_INSTANCE_ID_ALL
                        + "?jobinstanceid=" + this.launchedJobInstanceId)
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
                .port(this.getServerPort())
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
                .port(this.getServerPort())
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
                .port(this.getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_INFO_JOB_NAME, "simpleJob")
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testRestartJobExecution() {
        this.launchSimpleJobWithOutParameters();
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
                .port(this.getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_EXECUTIONS_RESTART, this.launchedJobExecutionId)
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testStopJobExecution() {
        this.launchSimpleBlockingJob();
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
                .port(this.getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_EXECUTIONS_STOP, this.launchedJobExecutionId)
                .then()
                .assertThat().statusCode(is(200));
        this.myThread.stop();
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
                .port(this.getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.STEP_EXECUTIONS, this.launchedStepExecutionId, this.launchedJobExecutionId)
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
                .port(this.getServerPort())
                .get(AbstractRestController.JobRestControllerAPI.JOB_PARAMETERS + "?jobname=simpleJob")
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testQueryJobExecutions() {
        final Date startDate = new Date(System.currentTimeMillis() - 100000);
        final Date endDate = new Date(System.currentTimeMillis() + 100000);
        final String exitStatus = ExitStatus.COMPLETED.getExitCode();
        final Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(QueryParameterKey.EXIT_STATUS, exitStatus);
        queryParameters.put(QueryParameterKey.START_DATE, startDate);
        queryParameters.put(QueryParameterKey.END_DATE, endDate);
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .filter(document("jobcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("jobname").description("The name of the Spring Batch Job"),
                                parameterWithName("resultsize").description("The maximum size of the result"))))
                .when()
                .port(this.getServerPort())
                .body(queryParameters)
                .post(AbstractRestController.JobRestControllerAPI.QUERY_JOB_EXECUTIONS +
                        "?jobname=simpleJob&resultsize=4")
                .then()
                .assertThat().statusCode(is(200));
    }
}


