package org.tuxdevelop.spring.batch.lightmin.admin.event;

import lombok.Getter;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;


public class JobExecutionEvent extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private final JobExecution jobExecution;

    public JobExecutionEvent(final JobExecution jobExecution) {
        super(jobExecution);
        this.jobExecution = jobExecution;
    }
}
