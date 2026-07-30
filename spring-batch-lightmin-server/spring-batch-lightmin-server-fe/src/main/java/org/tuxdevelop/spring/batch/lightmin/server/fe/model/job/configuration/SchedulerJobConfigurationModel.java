package org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler.JobSchedulerModel;

import jakarta.validation.Valid;

@Data
@EqualsAndHashCode(callSuper = true)
public class SchedulerJobConfigurationModel extends CommonJobConfigurationModel {

    @Valid
    private JobSchedulerModel config;
}
