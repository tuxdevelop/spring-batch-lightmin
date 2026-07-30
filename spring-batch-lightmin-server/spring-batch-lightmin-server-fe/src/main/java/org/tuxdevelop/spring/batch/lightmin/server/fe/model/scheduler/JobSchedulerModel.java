package org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.TaskExecutorTypeModel;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.IsCronExpression;

import jakarta.validation.constraints.AssertTrue;

@Data
public class JobSchedulerModel {

    private String type;
    private SchedulerTypeModel typeRead;
    @IsCronExpression
    private String cronExpression;
    private Long initialDelay;
    private Long fixedDelay;
    private String taskExecutor;
    private TaskExecutorTypeModel taskExecutorRead;
    private SchedulerStatusModel statusRead;
    private String status;

    //View Helpers

    public Boolean getIsStoppable() {
        final Boolean stoppable;
        if (SchedulerStatusModel.SchedulerStatus.RUNNING.getDisplayText().equals(this.statusRead.getDisplayText())) {
            stoppable = Boolean.TRUE;
        } else {
            stoppable = Boolean.FALSE;
        }
        return stoppable;
    }

    public Boolean getIsStartable() {
        final Boolean startable;
        if (SchedulerStatusModel.SchedulerStatus.STOPPED.getDisplayText().equals(this.statusRead.getDisplayText()) ||
                SchedulerStatusModel.SchedulerStatus.INITIALIZED.getDisplayText().equals(this.statusRead.getDisplayText())) {
            startable = Boolean.TRUE;
        } else {
            startable = Boolean.FALSE;
        }
        return startable;
    }

    public Boolean getIsCron() {
        return SchedulerTypeModel.JobSchedulerType.CRON.name().equals(this.type);
    }

    public Boolean getIsPeriod() {
        return SchedulerTypeModel.JobSchedulerType.PERIOD.name().equals(this.type);
    }

    //Validation helpers
    @Setter(AccessLevel.NONE)
    private boolean initialDelayValid;
    @Setter(AccessLevel.NONE)
    private boolean fixedDelayValid;
    @Setter(AccessLevel.NONE)
    private boolean cronExpressionDelayValid;

    @AssertTrue(message = "Initial Delay must not be null and must be >= 0")
    public boolean isInitialDelayValid() {
        final Boolean result;
        if (SchedulerTypeModel.JobSchedulerType.PERIOD.name().equals(this.type)) {
            if (this.initialDelay == null) {
                result = Boolean.FALSE;
            } else {
                result = this.initialDelay >= 0;
            }
        } else {
            result = Boolean.TRUE;
        }

        return result;
    }

    @AssertTrue(message = "Fixed Delay must not be null and must be greater than 0")
    public boolean isFixedDelayValid() {
        final Boolean result;
        if (SchedulerTypeModel.JobSchedulerType.PERIOD.name().equals(this.type)) {
            if (this.fixedDelay == null) {
                result = Boolean.FALSE;
            } else {
                result = this.fixedDelay > 0;
            }
        } else {
            result = Boolean.TRUE;
        }

        return result;
    }
}
