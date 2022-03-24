package ru.msu.cmc.realestatemanager.model.util;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Map;

@Converter
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attrMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        String customerInfoJson = null;
        try {
            customerInfoJson = objectMapper.writeValueAsString(attrMap);
        } catch (final JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        return customerInfoJson;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String customerInfoJSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> customerInfo = null;
        try {
            customerInfo = objectMapper.readValue(customerInfoJSON, Map.class);
        } catch (final IOException e) {
            System.out.println(e.getMessage());
        }

        return customerInfo;
    }

}
