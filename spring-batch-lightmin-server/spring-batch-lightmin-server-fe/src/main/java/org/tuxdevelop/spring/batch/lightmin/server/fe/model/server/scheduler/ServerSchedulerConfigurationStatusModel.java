package org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler;

import lombok.Getter;

public class ServerSchedulerConfigurationStatusModel {

    @Getter
    private final String displayText;
    @Getter
    private final String value;

    public ServerSchedulerConfigurationStatusModel(final ServerSchedulerConfigurationStatusType serverSchedulerExecutionType) {
        this.displayText = serverSchedulerExecutionType.displayText;
        this.value = serverSchedulerExecutionType.value;
    }

    public enum ServerSchedulerConfigurationStatusType {
        ACTIVE("ACTIVE", "active"),
        STOPPED("STOPPED", "stopped");

        @Getter
        private final String displayText;
        @Getter
        private final String value;

        ServerSchedulerConfigurationStatusType(final String value, final String displayText) {
            this.value = value;
            this.displayText = displayText;
        }
    }

    public Boolean getIsStoppable() {
        return ServerSchedulerConfigurationStatusType.ACTIVE.value.equalsIgnoreCase(this.value);
    }

    public Boolean getIsStartable() {
        return ServerSchedulerConfigurationStatusType.STOPPED.value.equalsIgnoreCase(this.value);
    }


    public static ServerSchedulerConfigurationStatusType map(final String value) {
        return ServerSchedulerConfigurationStatusType.valueOf(value);
    }
}
