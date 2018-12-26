package org.tuxdevelop.spring.batch.lightmin.documentation.api.client;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobLaunch;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.client.api.controller.AbstractRestController;
import org.tuxdevelop.spring.batch.lightmin.documentation.api.AbstractServiceDocumentation;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class JobLauncherRestControllerDocumentation extends AbstractServiceDocumentation {

    @Test
    public void testlaunchJob() {
        final JobParameters jobParameters = new JobParameters();
        final Map<String, JobParameter> parametersMap = new HashMap<>();
        final JobParameter jobParameter = new JobParameter();
        jobParameter.setParameter("~/input/current_file.csv");
        jobParameter.setParameterType(ParameterType.STRING);
        parametersMap.put("pathToTargetFile", jobParameter);
        jobParameters.setParameters(parametersMap);
        final JobLaunch jobLaunch = new JobLaunch();
        jobLaunch.setJobName("simpleJob");
        jobLaunch.setJobParameters(jobParameters);
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .filter(document("joblaunchercontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .when()
                .port(this.getServerPort())
                .body(jobLaunch)
                .post(AbstractRestController.JobLauncherRestControllerAPI.JOB_LAUNCH)
                .then()
                .assertThat().statusCode(is(201));

    }

}
