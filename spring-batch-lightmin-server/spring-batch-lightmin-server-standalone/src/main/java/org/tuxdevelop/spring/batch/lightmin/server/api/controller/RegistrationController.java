package org.tuxdevelop.spring.batch.lightmin.server.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
@ResponseBody
@RequestMapping(value = "api/applications")
public class RegistrationController {

    private final RegistrationBean registrationBean;

    public RegistrationController(final RegistrationBean registrationBean) {
        this.registrationBean = registrationBean;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LightminClientApplication> register(
            @RequestBody final LightminClientApplication lightminClientApplication) {
        log.debug("Register LightminClientApplication {}", lightminClientApplication);
        final LightminClientApplication registeredLightminClientApplication = this.registrationBean
                .register(lightminClientApplication);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredLightminClientApplication);
    }

    @RequestMapping(value = "/{applicationid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> unregister(@PathVariable(value = "applicationid") final String applicationId) {
        log.debug("Unregister LightminClientApplication with id {}", applicationId);
        final LightminClientApplication lightminClientApplication = this.registrationBean.deleteRegistration(applicationId);
        if (lightminClientApplication != null) {
            return ResponseEntity.ok(lightminClientApplication);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LightminClientApplication[]> getAll() {
        final Collection<LightminClientApplication> lightminClientApplications = this.registrationBean.findAll();
        return ResponseEntity.ok(
                lightminClientApplications.toArray(new LightminClientApplication[lightminClientApplications.size()]));
    }
}
