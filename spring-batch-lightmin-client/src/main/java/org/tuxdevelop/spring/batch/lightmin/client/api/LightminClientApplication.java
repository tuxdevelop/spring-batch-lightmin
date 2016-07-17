package org.tuxdevelop.spring.batch.lightmin.client.api;


import lombok.Data;

import java.io.Serializable;

@Data
public class LightminClientApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String managementUrl;
    private String healthUrl;
    private String serviceUrl;
    private LightminClientInformation lightminClientInformation;


}
