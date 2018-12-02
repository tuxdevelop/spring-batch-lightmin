package org.tuxdevelop.spring.batch.lightmin.server.fe.model.common;

import lombok.Data;
import lombok.Getter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;

@Data
public class JobIncremeterTypeModel {

    private String displayText;
    private String value;

    public JobIncremeterTypeModel(final JobIncremeterType type) {
        this.displayText = type.displayText;
        this.value = type.name();
    }

    public enum JobIncremeterType {

        DATE("date"),
        NONE("no incremeter");

        @Getter
        private final String displayText;

        JobIncremeterType(final String displayText) {
            this.displayText = displayText;
        }
    }

    public static JobIncremeterType map(final JobIncrementer incrementer) {
        return JobIncremeterType.valueOf(incrementer.name());
    }
}
