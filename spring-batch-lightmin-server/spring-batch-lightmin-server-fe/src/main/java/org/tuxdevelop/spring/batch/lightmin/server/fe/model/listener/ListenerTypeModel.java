package org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener;

import lombok.Data;
import lombok.Getter;

@Data
public class ListenerTypeModel {

    private String displayText;
    private String value;

    public ListenerTypeModel(final JobListenerType jobListenerType) {
        this.displayText = jobListenerType.displayText;
        this.value = jobListenerType.name();
    }

    public enum JobListenerType {

        LOCAL_FOLDER_LISTENER("local folder listener");

        @Getter
        private final String displayText;

        JobListenerType(final String displayText) {
            this.displayText = displayText;
        }
    }

    public static JobListenerType map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobListenerType type) {
        return JobListenerType.valueOf(type.name());
    }
}
