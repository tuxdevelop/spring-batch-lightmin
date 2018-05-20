package org.tuxdevelop.spring.batch.lightmin.admin.repository;

import org.tuxdevelop.spring.batch.lightmin.configuration.SpringBatchLightminConfigurationProperties;

public class UrlRemoteJobConfigurationRepositoryLocator implements RemoteJobConfigurationRepositoryLocator {

    private final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties;

    public UrlRemoteJobConfigurationRepositoryLocator(final SpringBatchLightminConfigurationProperties springBatchLightminConfigurationProperties) {
        this.springBatchLightminConfigurationProperties = springBatchLightminConfigurationProperties;
    }

    @Override
    public String getRemoteUrl() {
        return this.springBatchLightminConfigurationProperties.getRemoteRepositoryServerUrl();
    }
}
