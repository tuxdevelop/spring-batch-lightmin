package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
public class JobInstanceModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String jobName;
	private Collection<JobExecutionModel> jobExecutions;

}
