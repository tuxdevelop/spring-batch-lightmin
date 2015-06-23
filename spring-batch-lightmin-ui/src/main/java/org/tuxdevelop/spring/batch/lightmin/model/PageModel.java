package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer startIndex;
	private Integer pageSize;

}
