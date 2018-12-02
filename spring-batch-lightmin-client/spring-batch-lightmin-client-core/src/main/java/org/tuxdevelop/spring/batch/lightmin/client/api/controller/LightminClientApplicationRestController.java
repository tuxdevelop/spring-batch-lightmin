package org.tuxdevelop.spring.batch.lightmin.client.api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.service.LightminClientApplicationService;

@ResponseBody
@RequestMapping("/api/lightminclientapplications")
public class LightminClientApplicationRestController {

    private final LightminClientApplicationService lightminClientApplicationService;

    public LightminClientApplicationRestController(final LightminClientApplicationService lightminClientApplicationService) {
        this.lightminClientApplicationService = lightminClientApplicationService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LightminClientApplication> getLightminClientApplication() {
        final LightminClientApplication lightminClientApplication = this.lightminClientApplicationService
                .getLightminClientApplication();
        return ResponseEntity.ok(lightminClientApplication);
    }
}
