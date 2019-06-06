package org.tuxdevelop.spring.batch.lightmin.api.resource.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Marcel Becker
 * @Since 0.3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobParameter implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Object parameter;
    private ParameterType parameterType;
}

class DateDeserializer extends JsonDeserializer<Object> {

    private final TypeReference<Object> typeRef = new TypeReference<Object>() {
    };

    @Override
    public Object deserialize(final JsonParser p, final DeserializationContext ctxt, final Object target) throws IOException {
        final Object value = new ObjectMapper().readValue(p, this.typeRef);

        final Object result;

        if (value != null) {
            final String stringValue = value.toString();
            if (stringValue.endsWith(DateSerializer.DATE_SUFFIX)) {
                result = DomainParameterParser.parseDate(stringValue.substring(0, stringValue.length() - 6));
            } else {
                result = value;
            }
        } else {
            result = value;
        }
        return result;
    }

    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
        return this.deserialize(jsonParser, deserializationContext, null);
    }
}

class DateSerializer extends JsonSerializer<Object> {

    static final String DATE_SUFFIX = "[date]";

    @Override
    public void serialize(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (o instanceof Date) {
            final Date date = (Date) o;
            final String dateValue = DomainParameterParser.parseDate(date);
            new ObjectMapper().writeValue(jsonGenerator, dateValue + DATE_SUFFIX);
        } else {
            new ObjectMapper().writeValue(jsonGenerator, o);
        }

    }
}


