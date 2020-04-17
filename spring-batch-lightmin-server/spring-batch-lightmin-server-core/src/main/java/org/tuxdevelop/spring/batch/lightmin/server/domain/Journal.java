package org.tuxdevelop.spring.batch.lightmin.server.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Journal implements Serializable {

    private static final long serialVersionUID = -1L;
    
    private Long id;
    private String applicationName;
    private String host;
    private String oldStatus;
    private String newStatus;
    private Date timestamp;

}
