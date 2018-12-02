package org.tuxdevelop.spring.batch.lightmin.event;

import lombok.Getter;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;


public class JobExecutionEvent extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private final JobExecution jobExecution;
    @Getter
    private final String applicationName;

    public JobExecutionEvent(final JobExecution jobExecution, final String applicationName) {
        super(jobExecution);
        this.jobExecution = jobExecution;
        this.applicationName = applicationName;
    }
}
