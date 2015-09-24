package org.tuxdevelop.spring.batch.lightmin.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PageModelTest {

    @Test
    public void testNextStartIndex() {
        final PageModel pageModel = new PageModel(0, 10, 11);
        assertThat(pageModel.getNextStartIndex()).isEqualTo(10);
    }

    @Test
    public void testNextStartIndexSinglePage() {
        final PageModel pageModel = new PageModel(0, 10, 10);
        assertThat(pageModel.getNextStartIndex()).isEqualTo(0);
    }

    @Test
    public void testPreviousStartIndex() {
        final PageModel pageModel = new PageModel(9, 10, 15);
        assertThat(pageModel.getPreviousStartIndex()).isEqualTo(0);
    }

    @Test
    public void testPreviousStartIndexSinglePage() {
        final PageModel pageModel = new PageModel(4, 10, 15);
        assertThat(pageModel.getPreviousStartIndex()).isEqualTo(0);
    }

    @Test
    public void testHasMore() {
        final PageModel pageModel = new PageModel(0, 10, 11);
        assertThat(pageModel.getHasMore()).isTrue();
    }

    @Test
    public void testHasNotMore() {
        final PageModel pageModel = new PageModel(0, 10, 10);
        assertThat(pageModel.getHasMore()).isFalse();
    }

    @Test
    public void testHasPrevious() {
        final PageModel pageModel = new PageModel(10, 10, 11);
        assertThat(pageModel.getHasPrevious()).isTrue();
    }

    @Test
    public void testHasNotPrevious() {
        final PageModel pageModel = new PageModel(0, 10, 10);
        assertThat(pageModel.getHasPrevious()).isFalse();
    }
}