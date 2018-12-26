package org.tuxdevelop.spring.batch.lightmin.server.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Journal {

    private Long id;
    private String applicationName;
    private String host;
    private String oldStatus;
    private String newStatus;
    private Date timestamp;

}
