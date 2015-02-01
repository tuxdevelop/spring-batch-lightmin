package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import lombok.Getter;

public enum JobIncrementer {

	DATE("DATE_INCREMENTER");

	@Getter
	private String incrementerIdentifier;

	private JobIncrementer(final String incrementerIdentifier) {
		this.incrementerIdentifier = incrementerIdentifier;
	}
}
