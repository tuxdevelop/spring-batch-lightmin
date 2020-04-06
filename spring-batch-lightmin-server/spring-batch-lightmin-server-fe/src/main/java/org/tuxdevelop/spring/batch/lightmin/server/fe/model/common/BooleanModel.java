package org.tuxdevelop.spring.batch.lightmin.server.fe.model.common;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Data
public class BooleanModel {

    @Getter
    private Boolean value;
    @Getter
    private String displayText;

    public BooleanModel(final Boolean value, final String displayText) {
        this.value = value;
        this.displayText = displayText;
    }

    public static List<BooleanModel> values() {
        return Arrays.asList(
                new BooleanModel(Boolean.TRUE, Boolean.TRUE.toString().toLowerCase()),
                new BooleanModel(Boolean.FALSE, Boolean.FALSE.toString().toLowerCase())
        );
    }

}
