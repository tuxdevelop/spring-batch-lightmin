package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import lombok.Getter;

public enum JobIncrementer {

	DATE("DATE_INCREMETER");

	@Getter
	private String incremeterIdentifier;

	private JobIncrementer(final String incrementIdentifier) {
		this.incremeterIdentifier = incrementIdentifier;
	}
}
