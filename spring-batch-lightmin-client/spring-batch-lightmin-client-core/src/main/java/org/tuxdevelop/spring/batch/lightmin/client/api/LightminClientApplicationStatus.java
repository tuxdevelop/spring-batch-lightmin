package org.tuxdevelop.spring.batch.lightmin.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@EqualsAndHashCode
@ToString
public class LightminClientApplicationStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    private final String status;
    @Getter
    private final Long timeInMills;

    private LightminClientApplicationStatus(final String status, final long timeInMills) {
        this.status = status.toUpperCase();
        this.timeInMills = timeInMills;
    }

    public static LightminClientApplicationStatus valueOf(final String status) {
        return new LightminClientApplicationStatus(status, System.currentTimeMillis());
    }

    @JsonCreator
    public static LightminClientApplicationStatus valueOf(@JsonProperty("status") final String status,
                                                          @JsonProperty("timeInMills") final long timeInMills) {
        return new LightminClientApplicationStatus(status, timeInMills);
    }

    public static LightminClientApplicationStatus ofUnknown() {
        return valueOf("UNKNOWN");
    }

    public static LightminClientApplicationStatus ofUp() {
        return valueOf("UP");
    }

    public static LightminClientApplicationStatus ofDown() {
        return valueOf("DOWN");
    }

    public static LightminClientApplicationStatus ofOffline() {
        return valueOf("OFFLINE");
    }

}