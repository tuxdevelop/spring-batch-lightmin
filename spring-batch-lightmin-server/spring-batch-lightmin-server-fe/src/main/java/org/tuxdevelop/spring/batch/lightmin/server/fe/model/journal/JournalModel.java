package org.tuxdevelop.spring.batch.lightmin.server.fe.model.journal;

import lombok.Data;

import java.util.Date;

@Data
public class JournalModel {

    private String applicationName;
    private String host;
    private String oldStatus;
    private String newStatus;
    private Date timestamp;
}
