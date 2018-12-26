package org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler;

import lombok.Data;
import lombok.Getter;

@Data
public class SchedulerStatusModel {

    private String displayText;
    private String value;

    public SchedulerStatusModel(final SchedulerStatus schedulerStatus) {
        this.displayText = schedulerStatus.displayText;
        this.value = schedulerStatus.name();
    }

    public enum SchedulerStatus {

        INITIALIZED("initialized"),
        RUNNING("running"),
        STOPPED("stopped"),
        IN_TERMINATION("in terminination");

        @Getter
        private final String displayText;

        SchedulerStatus(final String displayText) {
            this.displayText = displayText;
        }
    }

    public static SchedulerStatus map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.SchedulerStatus status) {
        return SchedulerStatus.valueOf(status.name());
    }
}
