package org.tuxdevelop.spring.batch.lightmin.admin;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class JobConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long jobConfigurationId;
	private String jobName;
	private JobSchedulerConfiguration jobSchedulerConfiguration;
	private Map<String, Object> jobParameters;
	private Boolean addDateParameter;

}
