package org.tuxdevelop.spring.batch.lightmin.api.resource.admin;

import lombok.Data;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Data
public class JobListenerConfiguration {

    private JobListenerType jobListenerType;
    private String sourceFolder;
    private String filePattern;
    private Long pollerPeriod;
    private ListenerStatus listenerStatus;
    private TaskExecutorType taskExecutorType;

}
