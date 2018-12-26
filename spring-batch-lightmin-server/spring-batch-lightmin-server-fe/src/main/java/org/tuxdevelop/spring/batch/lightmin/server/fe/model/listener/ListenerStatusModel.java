package org.tuxdevelop.spring.batch.lightmin.server.fe.model.listener;

import lombok.Data;
import lombok.Getter;

@Data
public class ListenerStatusModel {

    private String displayText;
    private String value;

    public ListenerStatusModel(final ListenerStatus listenerStatus) {
        this.displayText = listenerStatus.displayText;
        this.value = listenerStatus.name();
    }

    public enum ListenerStatus {

        ACTIVE("active"),
        STOPPED("stopped");

        @Getter
        private final String displayText;

        ListenerStatus(final String displayText) {
            this.displayText = displayText;
        }
    }

    public static ListenerStatus map(final org.tuxdevelop.spring.batch.lightmin.api.resource.admin.ListenerStatus status) {
        return ListenerStatus.valueOf(status.name());
    }
}
