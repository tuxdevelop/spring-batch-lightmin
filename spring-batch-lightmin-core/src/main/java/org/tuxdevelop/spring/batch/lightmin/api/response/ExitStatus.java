package org.tuxdevelop.spring.batch.lightmin.api.response;


import lombok.Getter;

import java.io.Serializable;

/**
 * Copied from {@Link org.springframework.batch.core.ExitStatus}
 * The existing class has to be adapted by an Default Constructor (Jackson Mapper)
 */
public class ExitStatus implements Serializable {

    @Getter
    private final String exitCode;
    @Getter
    private final String exitDescription;

    public ExitStatus() {
        this("", "");
    }

    public ExitStatus(final String exitCode) {
        this(exitCode, "");
    }

    public ExitStatus(final String exitCode, final String exitDescription) {
        super();
        this.exitCode = exitCode;
        this.exitDescription = exitDescription == null ? "" : exitDescription;
    }
}
