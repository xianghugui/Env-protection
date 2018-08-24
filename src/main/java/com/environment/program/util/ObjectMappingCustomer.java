package com.environment.program.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class ObjectMappingCustomer extends ObjectMapper {

    public ObjectMappingCustomer() {
        super();
        // 空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {

            @Override
            public void serialize(
                    Object value,
                    JsonGenerator jg,
                    SerializerProvider sp) throws IOException, JsonProcessingException {
                jg.writeString("");
            }
        });

    }
}

