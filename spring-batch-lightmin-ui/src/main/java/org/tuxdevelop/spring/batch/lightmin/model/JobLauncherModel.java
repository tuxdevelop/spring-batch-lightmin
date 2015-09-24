package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Data
public class JobLauncherModel {

	private String jobName;
	private String jobParameters;
}
