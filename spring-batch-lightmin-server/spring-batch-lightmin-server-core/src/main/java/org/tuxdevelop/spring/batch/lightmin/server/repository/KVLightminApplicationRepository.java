package org.tuxdevelop.spring.batch.lightmin.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplicationStatus;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.3
 */
@Slf4j
public abstract class KVLightminApplicationRepository<T extends Map<String, LightminClientApplication>> implements LightminApplicationRepository {

    private final T applications;

    public KVLightminApplicationRepository(final T applications) {
        this.applications = applications;
    }


    @Override
    public LightminClientApplication save(final LightminClientApplication lightminClientApplication) {
        this.applications.put(lightminClientApplication.getId(), lightminClientApplication);
        return this.copy(lightminClientApplication);
    }

    @Override
    public Collection<LightminClientApplication> findAll() {
        return this.copy(this.applications.values());
    }

    @Override
    public LightminClientApplication find(final String id) {
        return this.copy(this.applications.get(id));
    }

    @Override
    public Collection<LightminClientApplication> findByApplicationName(final String applicationName) {
        final Collection<LightminClientApplication> allApplications = this.findAll();
        final Collection<LightminClientApplication> lightminClientApplications = new ArrayList<>();
        for (final LightminClientApplication application : allApplications) {
            if (application.getName().equals(applicationName)) {
                lightminClientApplications.add(this.copy(application));
            } else {
                log.trace("Skipping");
            }
        }
        return lightminClientApplications;
    }

    @Override
    public LightminClientApplication delete(final String id) {
        return this.applications.remove(id);
    }

    @Override
    public void clear() {
        this.applications.clear();
    }

    private Collection<LightminClientApplication> copy(final Collection<LightminClientApplication> applications) {
        final List<LightminClientApplication> applicationList;
        if (applications == null || applications.isEmpty()) {
            applicationList = new ArrayList<>();
        } else {
            applicationList = new ArrayList<>();
            for (final LightminClientApplication application : applications) {
                applicationList.add(copy(application));
            }
        }
        return applicationList;
    }

    private LightminClientApplication copy(final LightminClientApplication application) {
        final LightminClientApplication copy;
        if (application != null) {
            copy = new LightminClientApplication();
            copy.setName(application.getName());
            copy.setHealthUrl(application.getHealthUrl());
            copy.setLightminClientApplicationStatus(this.copy(application.getLightminClientApplicationStatus()));
            copy.setLightminClientInformation(this.copy(application.getLightminClientInformation()));
            copy.setId(application.getId());
            copy.setManagementUrl(application.getManagementUrl());
            copy.setServiceUrl(application.getServiceUrl());
        } else {
            copy = null;
        }
        return copy;
    }

    private LightminClientApplicationStatus copy(final LightminClientApplicationStatus status) {
        return LightminClientApplicationStatus.valueOf(status);
    }

    private LightminClientInformation copy(final LightminClientInformation information) {
        final LightminClientInformation copy = new LightminClientInformation();
        copy.setExternalLinks(information.getExternalLinks());
        copy.setRegisteredJobs(information.getRegisteredJobs());
        copy.setSupportedApiFeatures(information.getSupportedApiFeatures());
        copy.setSupportedJobIncrementers(information.getSupportedJobIncrementers());
        copy.setSupportedJobListenerTypes(information.getSupportedJobListenerTypes());
        copy.setSupportedListenerStatuses(information.getSupportedListenerStatuses());
        copy.setSupportedSchedulerStatuses(information.getSupportedSchedulerStatuses());
        copy.setSupportedSchedulerTypes(information.getSupportedSchedulerTypes());
        copy.setSupportedTaskExecutorTypes(information.getSupportedTaskExecutorTypes());
        return copy;
    }
}
