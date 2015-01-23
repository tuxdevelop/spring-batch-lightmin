package org.tuxdevelop.spring.batch.lightmin.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class PageModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer startIndex;
	private Integer pageSize;

}
