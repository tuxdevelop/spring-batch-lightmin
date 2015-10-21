package org.tuxdevelop.spring.batch.lightmin.api.rest.response;

import lombok.Data;

import java.util.Map;

@Data
public class JobParametersResponse {

    private Map<String, JobParameterResponse> parameters;
}
