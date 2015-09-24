package org.tuxdevelop.spring.batch.lightmin.model;

import java.io.Serializable;

/**
 * @author Lars Thielmann
 * @since 0.1
 */
public class PageModel implements Serializable{

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
        int nextIndex = currentIndex;
        if (getHasMore()) {
            nextIndex = currentIndex + pageSize;
        }

        return nextIndex;
    }

    public Integer getPreviousStartIndex() {
        int previousIndex = currentIndex - pageSize;
        if (previousIndex < 0) {
            previousIndex = 0;
        }

        return previousIndex;
    }

    public Boolean getHasMore() {
        return (currentIndex + pageSize) < totalSize;
    }

    public Boolean getHasPrevious() {
        return currentIndex > 0;
    }
}
