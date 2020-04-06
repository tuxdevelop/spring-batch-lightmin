package org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler;

import lombok.Getter;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.exception.SchedulerRuntimException;

import java.util.Arrays;

public class ServerSchedulerExecutionStatusModel {

    @Getter
    private final String displayText;
    @Getter
    private final Integer value;

    public ServerSchedulerExecutionStatusModel(final ServerSchedulerExecutionType serverSchedulerExecutionType) {
        this.displayText = serverSchedulerExecutionType.displayText;
        this.value = serverSchedulerExecutionType.value;
    }

    public enum ServerSchedulerExecutionType {
        NEW(1, "new"),
        RUNNING(10, "running"),
        FINISHED(20, "finished"),
        FAILED(30, "failed"),
        LOST(40, "lost");

        @Getter
        private final String displayText;
        @Getter
        private final Integer value;

        ServerSchedulerExecutionType(final Integer value, final String displayText) {
            this.value = value;
            this.displayText = displayText;
        }
    }

    public static ServerSchedulerExecutionType map(final Integer status) {
        return Arrays.stream(ServerSchedulerExecutionType.values())
                .filter(
                        type -> type.value.equals(status)
                ).findFirst()
                .orElseThrow(
                        () -> new SchedulerRuntimException("Could not map ServerSchedulerStatus id " + status));
    }

    public Boolean getIsKillable() {
        return this.value != null &&
                (this.value.equals(ServerSchedulerExecutionType.NEW.value)
                        || this.value.equals(ServerSchedulerExecutionType.RUNNING.value));
    }

    public Boolean getIsDeletable() {
        return !this.value.equals(ServerSchedulerExecutionType.RUNNING.value);
    }
}
