package org.tuxdevelop.spring.batch.lightmin.domain;


import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

public class TaskExecutorTypeTest {

    @Test
    public void getByIdSYNCHRONOUSTest() {
        final TaskExecutorType taskExecutorType = TaskExecutorType.getById(1L);
        Assertions.assertThat(taskExecutorType).isEqualTo(TaskExecutorType.SYNCHRONOUS);
    }

    @Test
    public void getByIdASYNCHRONOUSTest() {
        final TaskExecutorType taskExecutorType = TaskExecutorType.getById(2L);
        Assertions.assertThat(taskExecutorType).isEqualTo(TaskExecutorType.ASYNCHRONOUS);
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void getByIdUnknownTest() {
        TaskExecutorType.getById(-100L);
    }
}
