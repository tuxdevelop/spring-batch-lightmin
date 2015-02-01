package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class JobConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long jobConfigurationId;
	private String jobName;
	private JobSchedulerConfiguration jobSchedulerConfiguration;
	private Map<String, Object> jobParameters;
	private JobIncrementer jobIncrementer;


}
