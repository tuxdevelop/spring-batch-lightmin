package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.PathExists;

import javax.validation.Valid;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Data
public class JobListenerConfiguration {

    private JobListenerType jobListenerType;
    @PathExists
    @Valid
    private String sourceFolder;
    private String filePattern;
    private Long pollerPeriod;
    private ListenerStatus listenerStatus;
    private TaskExecutorType taskExecutorType;

}
