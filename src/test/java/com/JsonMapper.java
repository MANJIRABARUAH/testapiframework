package com;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class JsonMapper {

    private final Map<String, Object> mapObject;
    private File jsonFile;

//    JsonMapper(File jsonFile) throws IOException {
//        mapObject = new ObjectMapper().readValue(jsonFile, new TypeReference<Map<String, Object>>() {
//        });
//    }

    JsonMapper(String jsonFilePath) throws IOException {
        this.jsonFile = new File(jsonFilePath);
        mapObject = new ObjectMapper().readValue(jsonFile, new TypeReference<Map<String, Object>>() {
        });
    }

    @SuppressWarnings("unused")
    public Object readJsonFor(String key) throws IOException {
        return mapObject.get(key);
    }

    Map<String, String> getDataMapper() {
        Map<String, String> data = new HashMap<>();
        for (String key : mapObject.keySet()) {
            if (mapObject.get(key) instanceof String) {
                data.put(key, (String) mapObject.get(key));
            }
        }
        return data;
    }

    String jsonToString() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(jsonFile.getPath());
        byte[] fileData = new byte[fileInputStream.available()];
        fileInputStream.read(fileData);
        fileInputStream.close();
        return new String(fileData, "UTF-8");
    }


    public static void main(String[] args) throws IOException {
        Map<String, Object> mapObject = new ObjectMapper().readValue(new File("D:\\test.txt"), new TypeReference<Map<String, Object>>() {
        });
        System.out.print(mapObject);
    }

}
