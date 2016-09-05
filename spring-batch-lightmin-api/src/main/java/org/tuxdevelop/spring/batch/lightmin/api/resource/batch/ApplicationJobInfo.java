package org.tuxdevelop.spring.batch.lightmin.api.resource.batch;

import lombok.Data;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Marcel Becker
 * @Since 0.3
 */
@Data
public class ApplicationJobInfo {

    private Collection<JobInfo> jobInfos;

    public ApplicationJobInfo() {
        jobInfos = new LinkedList<>();
    }
}
