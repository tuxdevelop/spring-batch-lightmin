package org.tuxdevelop.spring.batch.lightmin.api.rest.response;

import lombok.Data;

@Data
public class ExitStatusResponse {
    private String exitCode;
    private String exitDescription;
}
