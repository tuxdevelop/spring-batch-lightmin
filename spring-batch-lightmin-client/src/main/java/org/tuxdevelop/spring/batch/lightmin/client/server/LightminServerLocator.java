package org.tuxdevelop.spring.batch.lightmin.client.server;

import java.util.List;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public interface LightminServerLocator {

    List<String> getRemoteUrls();

}
