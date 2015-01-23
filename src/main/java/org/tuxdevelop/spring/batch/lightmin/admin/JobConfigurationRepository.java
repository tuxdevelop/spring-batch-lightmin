package org.tuxdevelop.spring.batch.lightmin.admin;

public interface JobConfigurationRepository {

	void delete(JobConfiguration jobConfiguration);
	void add(JobConfiguration jobConfiguration);

}
