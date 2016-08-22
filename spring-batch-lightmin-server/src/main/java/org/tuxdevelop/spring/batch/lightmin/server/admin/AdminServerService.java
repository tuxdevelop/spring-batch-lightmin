package org.tuxdevelop.spring.batch.lightmin.server.admin;


import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobConfiguration;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;

public interface AdminServerService {
    void saveJobConfiguration(JobConfiguration jobConfiguration, LightminClientApplication lightminClientApplication);
}
