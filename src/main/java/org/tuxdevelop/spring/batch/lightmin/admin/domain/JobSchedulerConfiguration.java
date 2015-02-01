package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import lombok.Data;

@Data
public class JobSchedulerConfiguration {

	private JobSchedulerType jobSchedulerType;
	private String cronExpression;
	private Long initialDelay;
	private Long fixedDelay;
    private TaskExecutorType taskExecutorType;
    private String beanName;
}
