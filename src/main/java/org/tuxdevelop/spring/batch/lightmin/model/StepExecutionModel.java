package org.tuxdevelop.spring.batch.lightmin.model;

import java.io.Serializable;

import org.springframework.batch.core.StepExecution;

import lombok.Data;

/**
 *
 * @author Marcel Becker
 *
 */
@Data
public class StepExecutionModel implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long jobInstanceId;
	private StepExecution stepExecution;
}
