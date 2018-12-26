package org.tuxdevelop.spring.batch.lightmin.repository;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public interface RemoteJobConfigurationRepositoryLocator {

    /**
     * This methode has to return a uniqzue url string for the Remote Repository Server.
     * <p>
     * Implementations
     * <ul>
     * <li>{@link UrlRemoteJobConfigurationRepositoryLocator}</li>
     * <li>{@link DiscoveryRemoteJobConfigurationRepositoryLocator}</li>
     * </ul>
     * </p>
     *
     * @return the remote url
     */
    String getRemoteUrl();

}
