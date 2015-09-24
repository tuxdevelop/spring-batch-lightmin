package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Data
public class JobInstanceModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String jobName;
	private Long jobInstanceId;
	private Collection<JobExecutionModel> jobExecutions;

}
