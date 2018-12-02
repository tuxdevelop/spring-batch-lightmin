package org.tuxdevelop.spring.batch.lightmin.server.fe.model.scheduler;

import lombok.Data;
import lombok.Getter;

@Data
public class SchedulerTypeModel {

    private String displayText;
    private String value;

    public SchedulerTypeModel(final JobSchedulerType jobSchedulerType) {
        this.displayText = jobSchedulerType.displayText;
        this.value = jobSchedulerType.name();
    }

    public enum JobSchedulerType {

        CRON("cron"),
        PERIOD("period");

        @Getter
        private final String displayText;

        JobSchedulerType(final String displayText) {
            this.displayText = displayText;
        }
    }

    public static JobSchedulerType map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobSchedulerType type) {
        return JobSchedulerType.valueOf(type.name());
    }
}
