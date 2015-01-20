package org.tuxdevelop.spring.batch.lightmin.model;

import java.io.Serializable;
import java.util.Collection;

import lombok.Data;

@Data
public class JobInstanceModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String jobName;
	private Long jobInstanceId;
	private Collection<JobExecutionModel> jobExecutions;

}
