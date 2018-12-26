package org.tuxdevelop.spring.batch.lightmin.documentation.api.server;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.documentation.api.AbstractServiceDocumentation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class RegistrationControllerDocumentation extends AbstractServiceDocumentation {

    @Test
    public void testRegister() {
        final LightminClientApplication lightminClientApplication = createLightminClientApplication("sample " +
                "application");
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .filter(document("registrationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .when()
                .port(getServerPort())
                .body(lightminClientApplication)
                .post("api/applications")
                .then()
                .assertThat().statusCode(is(201));
    }

    @Test
    public void testUnregister() {
        final LightminClientApplication lightminClientApplication = createLightminClientApplication("sample " + "application");
        final LightminClientApplication registeredApplication = registrationBean.register(lightminClientApplication);
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .filter(document("registrationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("applicationid").description("The id of the Lightmin Client " +
                                        "Application"))))
                .when()
                .port(getServerPort())
                .delete("api/applications/{applicationid}", registeredApplication.getId())
                .then()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void testgetAll() {
        final LightminClientApplication lightminClientApplication = createLightminClientApplication("sample " + "application");
        registrationBean.register(lightminClientApplication);
        given(this.documentationSpec)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .filter(document("registrationcontroller/{method-name}",
                        preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("localhost")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .when()
                .port(getServerPort())
                .get("api/applications")
                .then()
                .assertThat().statusCode(is(200));
    }
}
