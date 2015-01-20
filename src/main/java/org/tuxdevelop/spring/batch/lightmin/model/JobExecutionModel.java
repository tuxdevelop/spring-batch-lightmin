package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;
import org.springframework.batch.core.JobExecution;

import java.io.Serializable;

@Data
public class JobExecutionModel implements Serializable{

    private Long jobInstanceId;
    private JobExecution jobExecution;

}
