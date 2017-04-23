package com;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class JsonMapper {

    private final Map<String, Object> mapObject;

    JsonMapper(File jsonFile) throws IOException {
        mapObject = new ObjectMapper().readValue(jsonFile, new TypeReference<Map<String, Object>>() {
        });
    }

    @SuppressWarnings("unused")
    public Object readJsonFor(String key) throws IOException {
        return mapObject.get(key);
    }

    Map<String, String> getDataMapper() {
        Map<String, String> data = new HashMap<>();
        mapObject.forEach((key, value) -> {
            if (value instanceof String) data.put(key, (String) value);
        });
        return data;
    }

}
