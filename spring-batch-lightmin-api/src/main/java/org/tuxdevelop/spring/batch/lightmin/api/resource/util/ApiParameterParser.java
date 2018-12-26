package org.tuxdevelop.spring.batch.lightmin.api.resource.util;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters;
import org.tuxdevelop.spring.batch.lightmin.api.resource.common.ParameterType;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Marcel Becker
 * @since 1.0
 * <p>
 * Utility class to parse a String representation of job Parameters to {@link org.springframework.batch.core
 * .JobParameters} and {@link Map} of String, Object.
 * </p>
 * <p>
 * It is also possible to map {@link org.springframework.batch.core.JobParameters} and {@link Map} of
 * String and Object to human readble String parameters
 * </p>
 */
@Slf4j
public final class ApiParameterParser {

    private ApiParameterParser() {
    }

    /**
     * Maps {@link org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters} to a readable String
     *
     * @param jobParameters {@link org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters}
     * @return a human readble representation of the JobParamaters
     */
    public static String parseParametersToString(final org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameters jobParameters) {
        final Map<String, org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter> parameters = jobParameters.getParameters();
        final Map<String, Object> parameterMap = new HashMap<>();
        for (final Entry<String, org.tuxdevelop.spring.batch.lightmin.api.resource.common.JobParameter> entry : parameters.entrySet()) {
            parameterMap.put(entry.getKey(), entry.getValue().getParameter());
        }
        return DomainParameterParser.parseParameterMapToString(parameterMap);
    }

    /**
     * maps a String a parameters to Spring Batch {@link JobParameters
     * }.
     * The String has to have the following format:
     * <ul>
     * <li>
     * name(type)=value, name(type2)=value2
     * </li>
     * </ul>
     * The name is the job parameter name, the type, the Java type to the value.
     * Following Types are supported
     * <ul>
     * <li>
     * {@link String}
     * </li>
     * <li>
     * {@link Long}
     * </li>
     * <li>
     * {@link Double}
     * </li>
     * <li>
     * {@link Date
     * }
     * </li>
     * </ul>
     *
     * @param parameters String of parameters
     * @return Spring Batch {@link JobParameters}
     */
    public static JobParameters parseParametersToJobParameters(final String parameters) {
        final Map<String, JobParameter> jobParameterHashMap = new HashMap<>();
        final Map<String, Object> parametersMap = DomainParameterParser.parseParameters(parameters);
        for (final Entry<String, Object> entry : parametersMap.entrySet()) {
            final Object value = entry.getValue();
            if (value instanceof String) {
                jobParameterHashMap.put(entry.getKey(), new JobParameter(value, ParameterType.STRING));
            } else if (value instanceof Long || value instanceof Integer) {
                jobParameterHashMap.put(entry.getKey(), new JobParameter(value, ParameterType.LONG));
            } else if (value instanceof Date) {
                jobParameterHashMap.put(entry.getKey(), new JobParameter(value, ParameterType.DATE));
            } else if (value instanceof Double) {
                jobParameterHashMap.put(entry.getKey(), new JobParameter(value, ParameterType.DOUBLE));
            } else {
                throw new SpringBatchLightminApplicationException("Unknown Parameter type:"
                        + value.getClass().getName());
            }
        }
        return new JobParameters(jobParameterHashMap);
    }

}
