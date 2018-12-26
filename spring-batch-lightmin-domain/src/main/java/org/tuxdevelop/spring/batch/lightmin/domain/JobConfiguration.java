package org.tuxdevelop.spring.batch.lightmin.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class JobConfiguration extends AbstractConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long jobConfigurationId;
    private String jobName;
    private JobSchedulerConfiguration jobSchedulerConfiguration;
    private JobListenerConfiguration jobListenerConfiguration;
    private Map<String, Object> jobParameters;
    private JobIncrementer jobIncrementer;

    public void validateForSave() {
        if (this.jobConfigurationId != null) {
            this.throwExceptionAndLogError("jobConfigurationId must not be set for save");
        }
        this.validateCommon();
    }

    public void validateForUpdate() {
        if (this.jobConfigurationId == null) {
            this.throwExceptionAndLogError("jobConfigurationId must not be null for update");
        }
        this.validateCommon();
    }

    private void validateCommon() {
        if (this.jobName == null) {
            this.throwExceptionAndLogError("jobName must not be null");
        }
        if (this.jobSchedulerConfiguration == null) {
            if (this.jobListenerConfiguration == null) {
                this.throwExceptionAndLogError("jobSchedulerConfiguration or jobListenerConfiguration must not be null");
            } else {
                this.getJobListenerConfiguration().validate();
            }
        } else {
            this.jobSchedulerConfiguration.validate();
        }
    }

}
