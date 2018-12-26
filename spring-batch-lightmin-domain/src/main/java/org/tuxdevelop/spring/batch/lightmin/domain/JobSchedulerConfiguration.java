package org.tuxdevelop.spring.batch.lightmin.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.quartz.CronExpression;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
@EqualsAndHashCode
@ToString
public class JobSchedulerConfiguration extends AbstractConfiguration {

    private JobSchedulerType jobSchedulerType;
    private String cronExpression;
    private Long initialDelay;
    private Long fixedDelay;
    private TaskExecutorType taskExecutorType;
    private String beanName;
    private SchedulerStatus schedulerStatus;

    void validate() {
        if (this.jobSchedulerType == null) {
            this.throwExceptionAndLogError("jobSchedulerType must not be null");
        }
        if (this.taskExecutorType == null) {
            this.throwExceptionAndLogError("taskExecutorType must not be null");
        }
        if (JobSchedulerType.CRON.equals(this.jobSchedulerType)) {
            this.validateCron();
        } else if (JobSchedulerType.PERIOD.equals(this.jobSchedulerType)) {
            this.validatePeriod();
        } else {
            this.throwExceptionAndLogError("Unknown jobSchedulerType: " + this.jobSchedulerType);
        }
    }

    void validateCron() {
        if (this.cronExpression == null) {
            this.throwExceptionAndLogError("cronExpression must not be null for CRON Scheduler");
        } else if (!CronExpression.isValidExpression(this.cronExpression)) {
            this.throwExceptionAndLogError("cronExpression : " + this.cronExpression + " is not valid");
        }
        if (this.initialDelay != null) {
            this.throwExceptionAndLogError("initialDelay must not be set for CRON Scheduler");
        }
        if (this.fixedDelay != null) {
            this.throwExceptionAndLogError("fixedDelay must not be set for CRON Scheduler");
        }
    }

    void validatePeriod() {
        if (this.fixedDelay == null) {
            this.throwExceptionAndLogError("fixedDelay must not be null for PERIOD Scheduler");
        } else {
            if (this.fixedDelay <= 0) {
                this.throwExceptionAndLogError("fixedDelay must not be lower then 1");
            }
        }
        if (this.initialDelay != null && this.initialDelay <= 0) {
            this.throwExceptionAndLogError("initialDelay must not be lower then 1");
        }
        if (this.cronExpression != null) {
            this.throwExceptionAndLogError("cronExpression must not be set for PERIOD Scheduler");
        }
    }
}
