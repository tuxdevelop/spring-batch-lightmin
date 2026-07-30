package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.PathExists;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Data
public class JobListenerConfiguration {

    @NotNull
    private JobListenerType jobListenerType;
    @PathExists
    @Valid
    private String sourceFolder;
    @NotBlank
    private String filePattern;
    @Min(1)
    private Long pollerPeriod;
    @NotNull
    private ListenerStatus listenerStatus;
    @NotNull
    private TaskExecutorType taskExecutorType;

}
