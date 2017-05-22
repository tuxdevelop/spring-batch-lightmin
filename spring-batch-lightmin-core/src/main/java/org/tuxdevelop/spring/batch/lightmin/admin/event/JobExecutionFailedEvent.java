package org.tuxdevelop.spring.batch.lightmin.admin.event;

import org.springframework.batch.core.JobExecution;

import java.io.Serializable;


public class JobExecutionFailedEvent extends JobExecutionEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    public JobExecutionFailedEvent(final JobExecution jobExecution) {
        super(jobExecution);
    }
}
