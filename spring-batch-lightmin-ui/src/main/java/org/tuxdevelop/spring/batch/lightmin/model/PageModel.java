package org.tuxdevelop.spring.batch.lightmin.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class PageModel implements Serializable{

	private static final long serialVersionUID = 1L;
    private Integer currentIndex;
	private Integer pageSize;
    private Integer totalSize;
    private Boolean hasMore;

    public PageModel(final Integer startIndex, final Integer pageSize, final Integer totalSize) {
        this.currentIndex = startIndex;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
    }

    public Integer getNextStartIndex() {
        if (getHasMore()) {
            currentIndex += (pageSize - 1);
        }

        return currentIndex;
    }

    public Integer getPreviousStartIndex() {
        currentIndex -= (pageSize - 1);
        if (currentIndex < 0) {
            currentIndex = 0;
        }

        return currentIndex;
    }

    public Boolean getHasMore() {
        return ((currentIndex + 1) * pageSize) < totalSize;
    }

    public Boolean getHasPrevious() {
        return currentIndex > 0;
    }
}
