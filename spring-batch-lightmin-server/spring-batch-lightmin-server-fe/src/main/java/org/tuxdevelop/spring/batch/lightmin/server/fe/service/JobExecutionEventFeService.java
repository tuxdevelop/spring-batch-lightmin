package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.monitoring.JobExecutionEventInfo;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ContentPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.event.JobExecutionEventModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.EventService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.ArrayList;
import java.util.List;

public class JobExecutionEventFeService extends CommonFeService {

    private final EventService eventService;

    public JobExecutionEventFeService(final RegistrationBean registrationBean,
                                      final EventService eventService) {
        super(registrationBean);
        this.eventService = eventService;
    }

    public ContentPageModel<List<JobExecutionEventModel>> getJobExecutionEventModels(final Integer startIndex,
                                                                                     final Integer pageSize) {

        final List<JobExecutionEventInfo> events = this.eventService.getAllJobExecutionEvents(startIndex, pageSize);
        final Integer totalCount = this.eventService.getJobExecutionEventInfoCount();

        final ContentPageModel<List<JobExecutionEventModel>> contentPageModel =
                new ContentPageModel<>(startIndex, pageSize, totalCount);

        final List<JobExecutionEventModel> models = new ArrayList<>();

        for (final JobExecutionEventInfo event : events) {
            final JobExecutionEventModel model = new JobExecutionEventModel();
            model.setApplicationName(event.getApplicationName());
            model.setJobName(event.getJobName());
            model.setId(event.getJobExecutionId());
            model.setStartTime(event.getStartDate());
            model.setEndTime(event.getEndDate());
            model.setExitStatus(event.getExitStatus() != null ? event.getExitStatus().getExitCode().toLowerCase() : "");
            models.add(model);
            final String applicationInstanceId = this.getApplicationInstanceIdByName(event.getApplicationName());
            model.setApplicationInstanceId(applicationInstanceId);
        }

        contentPageModel.setValue(models);

        return contentPageModel;

    }

}
