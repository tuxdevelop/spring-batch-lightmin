package org.tuxdevelop.spring.batch.lightmin.admin;

import lombok.Data;

@Data
public class JobSchedulerConfiguration {

	private JobSchedulerType jobSchedulerType;
	private String cronExpression;
	private Long initialDelay;
	private Long fixedDelay;
}
