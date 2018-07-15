package org.tuxdevelop.spring.batch.lightmin.server.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.model.JobExecutionEventModel;
import org.tuxdevelop.spring.batch.lightmin.model.PageModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
@Controller
public class JobEventController {

    private final EventService eventService;

    public JobEventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/jobExecutionEvents")
    public void getJobExecutionEvents(
            final Model model,
            @RequestParam(value = "startindex", defaultValue = "0") final int startIndex,
            @RequestParam(value = "pagesize", defaultValue = "10") final int pageSize) {

        final List<JobExecutionEventInfo> jobExecutionInfos = this.eventService.getAllEvents(startIndex, pageSize);
        final List<JobExecutionEventModel> modelList = this.map(jobExecutionInfos);
        final PageModel jobEventPageModel =
                new PageModel(startIndex, pageSize, this.eventService.getJobExecutionEventInfoCount());

        model.addAttribute("jobExecutionEvents", modelList);
        model.addAttribute("pageModel", jobEventPageModel);
    }

    private List<JobExecutionEventModel> map(final List<JobExecutionEventInfo> infos) {
        final List<JobExecutionEventModel> modelList = new ArrayList<>();
        if (infos != null && !infos.isEmpty()) {
            for (final JobExecutionEventInfo info : infos) {
                final Optional<JobExecutionEventModel> model = this.map(info);
                if (model.isPresent()) {
                    modelList.add(model.get());
                } else {
                    log.debug("Skipping empty model for JobExecutionEventModel");
                }
            }
        } else {
            log.debug("Empty JobExecutionEventInfo list, nothing to map");
        }
        return modelList;
    }

    private Optional<JobExecutionEventModel> map(final JobExecutionEventInfo info) {
        final Optional<JobExecutionEventModel> result;

        if (info != null) {
            final JobExecutionEventModel model;
            model = new JobExecutionEventModel();
            model.setJobExecutionId(info.getJobExecutionId());
            model.setJobName(info.getJobName());
            model.setStartTime(info.getStartDate());
            model.setEndTime(info.getEndDate());
            model.setApplicationName(info.getApplicationName());
            model.setExitStatus(info.getExitStatus());
            result = Optional.of(model);
        } else {
            result = Optional.empty();
        }
        return result;
    }
}
