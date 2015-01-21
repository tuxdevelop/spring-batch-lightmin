package org.tuxdevelop.spring.batch.lightmin.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.Setter;

import org.springframework.batch.core.JobExecution;

@Data
public class JobExecutionModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long jobInstanceId;
	private String jobName;
	private JobExecution jobExecution;
	@Setter
	private String duration;

	public String getDuration() {
		final Date startTime = jobExecution.getStartTime();
		final Date endTime = jobExecution.getEndTime() != null ? jobExecution.getEndTime() : new Date();
		final Double duration = new Double(endTime.getTime() - startTime.getTime());
		final String durationString;
		if (duration > 1000) {
			durationString = (duration / 1000) + " s";
		} else {
			durationString = duration + " m";
		}
		return durationString;
	}

}
