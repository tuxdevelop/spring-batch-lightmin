package org.tuxdevelop.spring.batch.lightmin.api.response;

import java.io.Serializable;

import lombok.Getter;

/**
 * Copied from {@Link org.springframework.batch.core.ExitStatus} The existing
 * class has to be adapted by an Default Constructor (Jackson Mapper)
 */
public class ExitStatus implements Serializable {

    private static final long serialVersionUID = 1L;

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
