package org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener.JobListenerModel;

import jakarta.validation.Valid;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListenerJobConfigurationModel extends CommonJobConfigurationModel {

    @Valid
    private JobListenerModel config;
}
