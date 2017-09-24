package org.tuxdevelop.spring.batch.lightmin.client.discovery.lifecycle;

import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.tuxdevelop.spring.batch.lightmin.client.discovery.metadata.MetaDataExtender;

public class LightminEurekaClientConfigBean extends EurekaClientConfigBean {

    private final MetaDataExtender metaDataExtender;

    public LightminEurekaClientConfigBean(final MetaDataExtender metaDataExtender) {
        this.metaDataExtender = metaDataExtender;
        metaDataExtender.extendMetaData();
    }


}
