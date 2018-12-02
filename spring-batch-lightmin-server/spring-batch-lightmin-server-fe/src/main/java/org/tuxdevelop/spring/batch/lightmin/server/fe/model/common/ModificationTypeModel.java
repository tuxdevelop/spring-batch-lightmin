package org.tuxdevelop.spring.batch.lightmin.server.fe.model.common;

import lombok.Getter;

public class ModificationTypeModel {

    @Getter
    private final String value;

    public ModificationTypeModel(final ModificationType modificationType) {
        this.value = modificationType.getValue();
    }


    public enum ModificationType {
        READ("read"),
        ADD("add"),
        UPDATE("update");

        @Getter
        private final String value;

        ModificationType(final String value) {
            this.value = value;
        }
    }

}
