package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import lombok.Getter;

public enum JobSchedulerType {

	CRON(1L), PERIOD(2L);

	@Getter
	private Long id;

	private JobSchedulerType(final Long id) {
		this.id = id;
	}

	public JobSchedulerType getById(final Long id) {
		final JobSchedulerType type;
		if (CRON.getId().equals(id)) {
			type = CRON;
		} else if (PERIOD.getId().equals(id)) {
			type = PERIOD;
		} else {
			throw new IllegalArgumentException("Unknown id for JobSchedulerConfiguration:" + id);
		}
		return type;
	}

}
