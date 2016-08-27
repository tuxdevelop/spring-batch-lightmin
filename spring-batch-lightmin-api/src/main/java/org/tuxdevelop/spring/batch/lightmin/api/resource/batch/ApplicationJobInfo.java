package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import lombok.Data;

import java.util.Collection;
import java.util.LinkedList;

@Data
public class ApplicationJobInfo {

    private Collection<JobInfo> jobInfos;

    public ApplicationJobInfo() {
        jobInfos = new LinkedList<>();
    }
}
