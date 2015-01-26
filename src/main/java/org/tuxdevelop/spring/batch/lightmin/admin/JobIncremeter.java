package org.tuxdevelop.spring.batch.lightmin.admin;

import lombok.Getter;

public enum JobIncremeter {

	DATE("DATE_INCREMETER");

	@Getter
	private String incremeterIdentifier;

	private JobIncremeter(final String incrementIdentifier) {
		this.incremeterIdentifier = incrementIdentifier;
	}
}
