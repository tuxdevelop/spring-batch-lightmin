package org.tuxdevelop.spring.batch.lightmin.configuration;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.tuxdevelop.spring.batch.lightmin.service.JobService;
import org.tuxdevelop.spring.batch.lightmin.service.StepService;

@Slf4j
public abstract class AbstractSpringBatchLightminConfiguration {

	@Autowired(required = false)
	private DataSource dataSource;

	@Value("${table.prefix}")
	private String tablePrefix;

	@Bean
	public DefaultSpringBatchLightminConfiguration defaultSpringBatchLightminConfiguration() {
		final DefaultSpringBatchLightminConfiguration configuration;
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Create DefaultSpringBatchLightminConfiguration ");
		if (dataSource != null) {
			if (tablePrefix != null) {
				configuration = new DefaultSpringBatchLightminConfiguration(
						dataSource, tablePrefix);
				stringBuilder.append("with dataSource and tablePrefix: "
						+ tablePrefix);
			} else {
				configuration = new DefaultSpringBatchLightminConfiguration(
						dataSource);
				stringBuilder.append("with dataSource");
			}
		} else if (tablePrefix != null) {
			configuration = new DefaultSpringBatchLightminConfiguration(
					tablePrefix);
			stringBuilder.append("with tablePrefix: " + tablePrefix);
		} else {
			configuration = new DefaultSpringBatchLightminConfiguration();
		}
		log.info(stringBuilder.toString());
		return configuration;
	}

	@Bean
	public JobService jobService() {
		return defaultSpringBatchLightminConfiguration().getJobService();
	}

	@Bean
	public StepService stepService() {
		return defaultSpringBatchLightminConfiguration().getStepService();
	}

	@Bean
	public JobExecutionDao jobExecutionDao() {
		return defaultSpringBatchLightminConfiguration().getJobExecutionDao();
	}

	@Bean
	public JobInstanceDao jobInstanceDao() {
		return defaultSpringBatchLightminConfiguration().getJobInstanceDao();
	}

	@Bean
	public StepExecutionDao stepExecutionDao() {
		return defaultSpringBatchLightminConfiguration().getStepExecutionDao();
	}

	@Bean
	public JobOperator jobOperator() {
		return defaultSpringBatchLightminConfiguration().getJobOperator();
	}

	@Bean
	public JobLauncher jobLauncher() {
		return defaultSpringBatchLightminConfiguration().getJobLauncher();
	}

	@Bean
	public JobRegistry jobRegistry() {
		return defaultSpringBatchLightminConfiguration().getJobRegistry();
	}

	@Bean
	public JobExplorer jobExplorer() {
		return defaultSpringBatchLightminConfiguration().getJobExplorer();
	}

}
