package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

@Data
@EqualsAndHashCode(callSuper = false)
public class JobListenerConfiguration extends AbstractConfiguration {

    private JobListenerType jobListenerType;
    private String sourceFolder;
    private String filePattern;
    private Long pollerPeriod;
    private String beanName;
    private ListenerStatus listenerStatus;
    private TaskExecutorType taskExecutorType;

    public void validate() {
        if (jobListenerType == null) {
            throwExceptionAndLogError("jobListenerType must not be null");
        }
        if (pollerPeriod == null) {
            throwExceptionAndLogError("pollerPeriod must not be null");
        }
        if (JobListenerType.LOCAL_FOLDER_LISTENER.equals(jobListenerType)) {
            validateLocalFolderListener();
        }
    }

    private void validateLocalFolderListener() {
        if (!StringUtils.hasText(sourceFolder)) {
            throwExceptionAndLogError("sourceFolder must not be null or empty");
        }
        if (!StringUtils.hasText(filePattern)) {
            throwExceptionAndLogError("filePattern must not be null or empty");
        }
    }
}
