package org.tuxdevelop.spring.batch.lightmin.server.fe.model.common;

import lombok.Data;
import org.springframework.util.StringUtils;
import org.tuxdevelop.spring.batch.lightmin.util.DurationHelper;

import java.util.Date;

@Data
public abstract class CommonExecutionModel {

    protected static final String COMPLETED = "completed";
    protected static final String EXECUTING = "executing";
    protected static final String STARTED = "started";
    protected static final String UNKNOWN = "unknown";
    protected static final String FAILED = "failed";

    protected Long id;
    protected String status;
    protected String exitStatus;
    protected Date startTime;
    protected Date endTime;
    protected String exitMessage;

    public String getDuration() {
        return DurationHelper.createDurationValue(this.startTime, this.endTime);
    }

    public Boolean getIsCompleted() {
        return StringUtils.hasText(this.exitStatus) ? this.COMPLETED.equalsIgnoreCase(this.exitStatus) : Boolean.FALSE;
    }

    public Boolean getIsFinished() {
        return StringUtils.hasText(this.exitStatus) ?
                this.COMPLETED.equalsIgnoreCase(this.exitStatus) || this.FAILED.equalsIgnoreCase(this.exitStatus) :
                Boolean.FALSE;
    }

    public String getStatusClass() {
        return this.getStatusClassInternal(this.status);
    }

    public String getExitStatusClass() {
        return this.getStatusClassInternal(this.exitStatus);
    }

    private String getStatusClassInternal(final String status) {
        final String cssClass;
        if (StringUtils.hasText(status)) {
            if (this.COMPLETED.equalsIgnoreCase(status) || this.EXECUTING.equalsIgnoreCase(status)) {
                cssClass = "text-info";
            } else if (this.UNKNOWN.equalsIgnoreCase(status) || this.STARTED.equalsIgnoreCase(status)) {
                cssClass = "text-warning";
            } else {
                cssClass = "text-danger";
            }
        } else {
            cssClass = "text-warning";
        }
        return cssClass;
    }

}
