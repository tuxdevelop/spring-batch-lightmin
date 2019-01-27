package org.tuxdevelop.spring.batch.lightmin.server.fe.model.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ContentPageModel<T> extends PageModel {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private T value;

    public ContentPageModel(final Integer startIndex, final Integer pageSize, final Integer totalSize) {
        super(startIndex, pageSize, totalSize);
    }
}
