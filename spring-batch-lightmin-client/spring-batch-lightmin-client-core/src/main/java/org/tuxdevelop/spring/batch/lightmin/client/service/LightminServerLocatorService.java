package org.tuxdevelop.spring.batch.lightmin.client.service;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public interface LightminServerLocatorService {

    List<String> getRemoteUrls();

}
