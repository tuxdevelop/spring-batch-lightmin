package org.tuxdevelop.spring.batch.lightmin.admin;

import java.util.Collection;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.tuxdevelop.spring.batch.lightmin.admin.domain.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.execption.NoSuchJobConfigurationException;
import org.tuxdevelop.spring.batch.lightmin.execption.NoSuchJobException;

//TODO implement me
public class JdbcJobConfigurationRepository implements JobConfigurationRepository, InitializingBean {

	private final JdbcTemplate jdbcTemplate;

	private JdbcJobConfigurationRepository(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public JobConfiguration getJobConfiguration(final Long jobConfigurationId) throws NoSuchJobConfigurationException {
		return null;
	}

	@Override
	public Collection<JobConfiguration> getJobConfigurations(final String jobName) throws NoSuchJobException {
		return null;
	}

	@Override
	public JobConfiguration add(final JobConfiguration jobConfiguration) {
		return null;
	}

	@Override
	public JobConfiguration update(final JobConfiguration jobConfiguration) throws NoSuchJobConfigurationException {
		return null;
	}

	@Override
	public void delete(final JobConfiguration jobConfiguration) throws NoSuchJobException,
			NoSuchJobConfigurationException {

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		assert jdbcTemplate != null;
	}
}
