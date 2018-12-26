package org.tuxdevelop.spring.batch.lightmin.server.fe.model.common;

import lombok.Data;
import lombok.Getter;

@Data
public class TaskExecutorTypeModel {

    private String displayText;
    private String value;

    public TaskExecutorTypeModel(final TaskExecutorType taskExecutorType) {
        this.displayText = taskExecutorType.getDisplayText();
        this.value = taskExecutorType.name();
    }

    public enum TaskExecutorType {

        SYNCHRONOUS("synchronous"),
        ASYNCHRONOUS("asynchronous");

        @Getter
        private final String displayText;

        TaskExecutorType(final String displayText) {
            this.displayText = displayText;
        }
    }

    public static TaskExecutorType map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.TaskExecutorType type) {
        return TaskExecutorType.valueOf(type.name());
    }
}
