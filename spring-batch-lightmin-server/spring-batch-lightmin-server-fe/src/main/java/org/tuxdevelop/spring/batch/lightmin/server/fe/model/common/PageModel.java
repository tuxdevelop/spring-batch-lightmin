package org.tuxdevelop.spring.batch.lightmin.server.fe.model.common;

import java.io.Serializable;

/**
 * @author Lars Thielmann
 * @since 0.1
 */
public class PageModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Integer currentIndex;
    private final Integer pageSize;
    private final Integer totalSize;

    public PageModel(final Integer startIndex, final Integer pageSize, final Integer totalSize) {
        this.currentIndex = startIndex;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
    }

    public Integer getNextStartIndex() {
        int nextIndex = this.currentIndex;
        if (this.getHasMore()) {
            nextIndex = this.currentIndex + this.pageSize;
        }

        return nextIndex;
    }

    public Integer getPreviousStartIndex() {
        int previousIndex = this.currentIndex - this.pageSize;
        if (previousIndex < 0) {
            previousIndex = 0;
        }

        return previousIndex;
    }

    public Boolean getHasMore() {
        return (this.currentIndex + this.pageSize) < this.totalSize;
    }

    public Boolean getHasPrevious() {
        return this.currentIndex > 0;
    }
}
