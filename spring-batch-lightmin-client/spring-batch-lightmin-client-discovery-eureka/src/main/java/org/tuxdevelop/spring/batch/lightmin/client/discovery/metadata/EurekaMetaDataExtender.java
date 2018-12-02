package org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata;

import com.netflix.appinfo.ApplicationInfoManager;

import java.util.Map;

/**
 * @author Marcel Becker
 * @since 0.5
 */
public class EurekaMetaDataExtender implements MetaDataExtender {

    private final ApplicationInfoManager applicationInfoManager;

    public EurekaMetaDataExtender(final ApplicationInfoManager applicationInfoManager) {
        this.applicationInfoManager = applicationInfoManager;
    }

    @Override
    public void extendMetaData() {
        final Map<String, String> map = this.applicationInfoManager.getInfo().getMetadata();
        map.put(LIGHTMIN_CLIENT_META_DATA_KEY, LIGHTMIN_CLIENT_META_DATA_VALUE);
    }
}