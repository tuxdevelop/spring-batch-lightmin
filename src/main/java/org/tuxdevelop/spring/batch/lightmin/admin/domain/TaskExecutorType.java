package org.tuxdevelop.spring.batch.lightmin.admin.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum TaskExecutorType {

    SYNCHRONOUS("SYNCHRONOUS"),
    ASYNCHRONOUS("ASYNCHRONOUS");

    @Getter
    private String value;

    private TaskExecutorType(final String value){
        this.value = value;
    }

    public List<TaskExecutorType> getAll(){
        return Arrays.asList(TaskExecutorType.values());
    }
}
