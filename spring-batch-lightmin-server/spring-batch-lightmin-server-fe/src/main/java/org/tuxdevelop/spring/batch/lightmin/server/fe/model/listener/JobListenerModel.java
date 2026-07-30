package org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.TaskExecutorTypeModel;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.validation.annotation.PathExists;

@Data
public class JobListenerModel {

    private String type;
    private ListenerTypeModel typeRead;
    @NotNull(message = "Source Folder must not be null or empty")
    @NotBlank(message = "Source Folder must not be null or empty")
    @PathExists
    private String sourceFolder;
    @NotNull(message = "File Pattern must not be null or empty")
    @NotBlank(message = "File Pattern must not be null or empty")
    private String filePattern;
    @Min(value = 0, message = "Poller Period must be greater than 0")
    @NotNull(message = "Poller Period must not be null")
    private Long pollerPeriod;
    private String taskExecutor;
    private TaskExecutorTypeModel taskExecutorRead;
    private ListenerStatusModel statusRead;
    private String status;

    public Boolean getIsStoppable() {
        final Boolean stoppable;
        if (ListenerStatusModel.ListenerStatus.ACTIVE.getDisplayText().equals(this.statusRead.getDisplayText())) {
            stoppable = Boolean.TRUE;
        } else {
            stoppable = Boolean.FALSE;
        }
        return stoppable;
    }

    public Boolean getIsStartable() {
        final Boolean startable;
        if (ListenerStatusModel.ListenerStatus.STOPPED.getDisplayText().equals(this.statusRead.getDisplayText())) {
            startable = Boolean.TRUE;
        } else {
            startable = Boolean.FALSE;
        }
        return startable;
    }
}
