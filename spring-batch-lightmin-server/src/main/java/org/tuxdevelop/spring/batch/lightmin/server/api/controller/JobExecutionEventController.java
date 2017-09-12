package org.tuxdevelop.spring.batch.lightmin.server.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
@ResponseBody
@RequestMapping(value = "api/events/jobexecutions")
public class JobExecutionEventController {

    private final EventService eventService;

    public JobExecutionEventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> consumeJobExecutionEvent(@RequestBody final JobExecutionEventInfo jobExecutionEventInfo) {
        this.eventService.handleJobExecutionEvent(jobExecutionEventInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
