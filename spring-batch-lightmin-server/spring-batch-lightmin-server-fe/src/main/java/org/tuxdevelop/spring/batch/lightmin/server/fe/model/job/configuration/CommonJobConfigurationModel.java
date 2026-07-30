package org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.configuration;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.JobIncremeterTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.validator.ValidJobParameters;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
public abstract class CommonJobConfigurationModel {

    private Long id;
    @NotNull
    private String jobName;
    @ValidJobParameters
    private String parameters;
    private Map<String, Object> parametersRead;
    private JobIncremeterTypeModel incrementerRead;
    private String incrementer;
}
