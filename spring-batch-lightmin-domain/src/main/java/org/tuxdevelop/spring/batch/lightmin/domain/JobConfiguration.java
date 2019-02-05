package org.tuxdevelop.spring.batch.lightmin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcel Becker
 * @version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class JobConfiguration extends AbstractConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long jobConfigurationId;
    private String jobName;
    private JobSchedulerConfiguration jobSchedulerConfiguration;
    private JobListenerConfiguration jobListenerConfiguration;
    @JsonProperty
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Map<String, Object> jobParameters;
    private JobIncrementer jobIncrementer;

    public void validateForSave() {
        if (this.jobConfigurationId != null) {
            this.throwExceptionAndLogError("jobConfigurationId must not be set for save");
        }
        this.validateCommon();
    }

    public void validateForUpdate() {
        if (this.jobConfigurationId == null) {
            this.throwExceptionAndLogError("jobConfigurationId must not be null for update");
        }
        this.validateCommon();
    }

    private void validateCommon() {
        if (this.jobName == null) {
            this.throwExceptionAndLogError("jobName must not be null");
        }
        if (this.jobSchedulerConfiguration == null) {
            if (this.jobListenerConfiguration == null) {
                this.throwExceptionAndLogError("jobSchedulerConfiguration or jobListenerConfiguration must not be null");
            } else {
                this.getJobListenerConfiguration().validate();
            }
        } else {
            this.jobSchedulerConfiguration.validate();
        }
    }

}

class DateDeserializer extends JsonDeserializer<Map<String, Object>> {

    static final String DATE_SUFFIX = "[date]";

    private final TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
    };

    @Override
    public Map<String, Object> deserialize(final JsonParser p, final DeserializationContext ctxt, final Map<String, Object> target) throws IOException {
        final Map<String, Object> map = new ObjectMapper().readValue(p, this.typeRef);

        for (final Map.Entry<String, Object> e : map.entrySet()) {
            if (e.getKey().endsWith(DATE_SUFFIX)) {
                final Date date = DomainParameterParser.parseDate((String) e.getValue());
                target.put(e.getKey().substring(0, e.getKey().length() - 6), date);
            } else {
                target.put(e.getKey(), e.getValue());
            }
        }

        return target;
    }

    @Override
    public Map<String, Object> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
        return this.deserialize(jsonParser, deserializationContext, new HashMap<>());
    }
}

@Slf4j
class DateSerializer extends JsonSerializer<Map<String, Object>> {

    @Override
    public void serialize(final Map<String, Object> value, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        final Map<String, Object> adaptedValue = new HashMap<>(value);

        for (final Map.Entry<String, Object> e : value.entrySet()) {
            if (e.getValue() instanceof Date) {
                final Date date = (Date) e.getValue();
                final String dateValue = DomainParameterParser.parseDate(date);
                adaptedValue.put(e.getKey() + DateDeserializer.DATE_SUFFIX, dateValue);
                adaptedValue.remove(e.getKey());
            } else {
                log.trace("skipping, no date");
            }
        }
        new ObjectMapper().writeValue(jsonGenerator, adaptedValue);
    }
}