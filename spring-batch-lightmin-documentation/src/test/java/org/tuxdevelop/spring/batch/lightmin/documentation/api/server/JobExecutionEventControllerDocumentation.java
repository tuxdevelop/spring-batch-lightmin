package org.tuxdevelop.spring.batch.lightmin.documentation.api.server;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.ExitStatus;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.documentation.api.AbstractServiceDocumentation;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;


public class JobExecutionEventControllerDocumentation extends AbstractServiceDocumentation {


    @Test
    public void testConsumeJobExecutionFailedEvent() {
        final ExitStatus exitStatus = new ExitStatus("FAILED", "failed for test");
        final JobExecutionEventInfo jobExecutionEventInfo = createJobExecutionEventInfo("testApplication", exitStatus);
        RestAssured.given(this.documentationSpec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .filter(document("jobexecutioncontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .when()
                .port(getServerPort())
                .body(jobExecutionEventInfo)
                .post("api/events/jobexecutions")
                .then()
                .assertThat().statusCode(is(HttpStatus.CREATED.value()));
    }

    private JobExecutionEventInfo createJobExecutionEventInfo(final String applicationName, final ExitStatus exitStatus) {
        final Date startDate = new Date();
        final Date endDate = new Date(startDate.getTime() + 10000);
        final JobExecutionEventInfo jobExecutionEventInfo = new JobExecutionEventInfo();
        jobExecutionEventInfo.setApplicationName(applicationName);
        jobExecutionEventInfo.setJobExecutionId(1L);
        jobExecutionEventInfo.setJobName("sampleJob");
        jobExecutionEventInfo.setStartDate(startDate);
        jobExecutionEventInfo.setEndDate(endDate);
        jobExecutionEventInfo.setExitStatus(exitStatus);
        return jobExecutionEventInfo;
    }

}
