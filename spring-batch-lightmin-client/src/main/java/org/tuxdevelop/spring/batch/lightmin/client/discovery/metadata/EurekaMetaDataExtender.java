package org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata;

import com.netflix.appinfo.ApplicationInfoManager;

import java.util.Map;

public class EurekaMetaDataExtender implements MetaDataExtender {

    @Override
    public void extendMetaData() {
        final Map<String, String> map = ApplicationInfoManager.getInstance().getInfo().getMetadata();
        map.put(LIGHTMIN_CLIENT_META_DATA_KEY, LIGHTMIN_CLIENT_META_DATA_VALUE);
    }
}