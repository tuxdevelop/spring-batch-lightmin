package org.tuxdevelop.spring.batch.lightmin.admin.domain;

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
        if (jobConfigurationId != null) {
            throwExceptionAndLogError("jobConfigurationId must not be set for save");
        }
        validateCommon();
    }

    public void validateForUpdate() {
        if (jobConfigurationId == null) {
            throwExceptionAndLogError("jobConfigurationId must not be null for update");
        }
        validateCommon();
    }

    private void validateCommon() {
        if (jobName == null) {
            throwExceptionAndLogError("jobName must not be null");
        }
        if (jobSchedulerConfiguration == null) {
            if (jobListenerConfiguration == null) {
                throwExceptionAndLogError("jobSchedulerConfiguration or jobListenerConfiguration must not be null");
            } else {
                getJobListenerConfiguration().validate();
            }
        } else {
            jobSchedulerConfiguration.validate();
        }
    }

}
