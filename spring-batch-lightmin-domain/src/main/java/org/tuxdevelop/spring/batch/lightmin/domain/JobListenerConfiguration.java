package org.tuxdevelop.spring.batch.lightmin.domain;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class JobListenerConfiguration extends AbstractConfiguration {

    private JobListenerType jobListenerType;
    private String sourceFolder;
    private String filePattern;
    private Long pollerPeriod;
    private String beanName;
    private ListenerStatus listenerStatus;
    private TaskExecutorType taskExecutorType;

    public void validate() {
        if (this.jobListenerType == null) {
            this.throwExceptionAndLogError("jobListenerType must not be null");
        }
        if (this.pollerPeriod == null) {
            this.throwExceptionAndLogError("pollerPeriod must not be null");
        }
        if (JobListenerType.LOCAL_FOLDER_LISTENER.equals(this.jobListenerType)) {
            this.validateLocalFolderListener();
        }
    }

    private void validateLocalFolderListener() {
        if (!StringUtils.hasText(this.sourceFolder)) {
            this.throwExceptionAndLogError("sourceFolder must not be null or empty");
        }
        if (!StringUtils.hasText(this.filePattern)) {
            this.throwExceptionAndLogError("filePattern must not be null or empty");
        }
    }
}
