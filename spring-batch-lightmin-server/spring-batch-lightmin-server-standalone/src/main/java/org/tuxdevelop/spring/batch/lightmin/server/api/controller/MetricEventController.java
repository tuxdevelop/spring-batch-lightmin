package org.tuxdevelop.spring.batch.lightmin.server.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.StepExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;

import java.util.List;

@Slf4j
@ResponseBody
@RequestMapping(value = "api/events/metrics")
public class MetricEventController {

    private final EventService eventService;

    public MetricEventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(path = "jobexecution", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> consumeMetricsJobExecutionEvent(@RequestBody final JobExecutionEventInfo jobExecutionEventInfo) {
        this.eventService.handleMetricEvent(jobExecutionEventInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "stepexecution", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> consumeMetricsJobExecutionEvent(@RequestBody final StepExecutionEventInfo stepExecutionEventInfo) {
        this.eventService.handleMetricEvent(stepExecutionEventInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
