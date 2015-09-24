package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Marcel Becker
 * @since 0.1
 */
@Data
public class JobInfoModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String jobName;
	private Integer instanceCount;

}
