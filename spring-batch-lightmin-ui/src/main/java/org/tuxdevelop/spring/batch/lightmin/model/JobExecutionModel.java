package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;
import lombok.Setter;
import org.springframework.batch.core.JobExecution;
import org.tuxdevelop.spring.batch.lightmin.util.DurationHelper;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Data
public class JobExecutionModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long jobInstanceId;
	private String jobName;
	private JobExecution jobExecution;
	private Collection<StepExecutionModel> stepExecutions;
	@Setter
	private String duration;

	public String getDuration() {
		this.duration = DurationHelper.createDurationValue(jobExecution.getStartTime(), jobExecution.getEndTime());
		return duration;
	}

}
