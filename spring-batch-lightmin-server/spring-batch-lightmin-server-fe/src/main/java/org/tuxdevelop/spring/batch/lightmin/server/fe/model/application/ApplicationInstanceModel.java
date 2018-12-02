package org.tuxdevelop.spring.batch.lightmin.server.fe.model.application;

import lombok.Data;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.RegisteredJobModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ApplicationInstanceModel {

    private static final String UP = "UP";
    private static final String DOWN = "DOWN";
    private static final String OFFLINE = "OFFLINE";

    private String id;
    private String name;
    private String serviceUrl;
    private String managementUrl;
    private String healthUrl;
    private Map<String, String> externalLinks;
    private String status;
    private List<RegisteredJobModel> registeredJobs;
    private ApplicationInstanceFeatureModel feature;

    public ApplicationInstanceModel() {
        this.externalLinks = new HashMap<>();
        this.registeredJobs = new ArrayList<>();
    }

    public Boolean getIsUp() {
        return this.status != null && UP.equals(this.status.toUpperCase());
    }

    public Boolean getIsDown() {
        return this.status != null && DOWN.equals(this.status.toUpperCase());
    }

    public Boolean getIsOffline() {
        return this.status != null && OFFLINE.equals(this.status.toUpperCase());
    }

    public Boolean getIsUnknown() {
        return !this.getIsDown() && !this.getIsOffline() && !this.getIsUp();
    }

}
