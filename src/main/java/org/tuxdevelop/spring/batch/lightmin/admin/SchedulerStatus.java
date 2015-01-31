package org.tuxdevelop.spring.batch.lightmin.admin;

import lombok.Getter;

public enum SchedulerStatus {

	INITIALIZED("INITIALIZED"), RUNNING("RUNNING"), STOPPED("STOPPED"), IN_TERMINATION("IN TERMINATION");

	@Getter
	private String value;

	private SchedulerStatus(final String value) {
		this.value = value;
	}
}
