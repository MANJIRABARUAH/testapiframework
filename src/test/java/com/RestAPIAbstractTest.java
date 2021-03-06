package com;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.SkipException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

abstract class RestAPIAbstractTest extends UseAsTestName_TestBase {

    static CloseableHttpClient httpClient;
    String testScriptName = null;
    String testDescription = null;
    String methodType = null;
    String parametrisedURL = null;
    String headerJSONFileName = null;
    String inputJSONFileName = null;
    String expectedResponseCode = null;
    String expectedJSONFileName = null;
    String execute = null;

    HttpResponse requestGET() throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder(parametrisedURL);

        if (inputJSONFileExists()) {
            JsonMapper paramMapper = new JsonMapper(getFilePath(inputJSONFileName));
            for (Map.Entry<String, String> entry : paramMapper.getDataMapper().entrySet()) {
                builder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        HttpGet getRequest = new HttpGet(builder.build());
        JsonMapper headerMapper = new JsonMapper(getFilePath(headerJSONFileName));

        for (Map.Entry<String, String> entry : headerMapper.getDataMapper().entrySet()) {
            getRequest.addHeader(entry.getKey(), entry.getValue());
        }
        return httpClient.execute(getRequest);
    }

    HttpResponse requestPOST() throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder(parametrisedURL);
        HttpPost postRequest = new HttpPost(builder.build());
        JsonMapper headerMapper = new JsonMapper(getFilePath(headerJSONFileName));

        for (Map.Entry<String, String> entry : headerMapper.getDataMapper().entrySet()) {
            postRequest.addHeader(entry.getKey(), entry.getValue());
        }


        JsonMapper dataMapper = new JsonMapper(getFilePath(inputJSONFileName));
        String jsonData = dataMapper.jsonToString();
        StringEntity jsonObj = new StringEntity(jsonData) {{
            setContentType("application/json");
        }};
        postRequest.setEntity(jsonObj);
        return httpClient.execute(postRequest);
    }

    private boolean inputJSONFileExists() {
        return !StringUtils.trimToEmpty(inputJSONFileName).isEmpty();
    }

    private String getFilePath(String inputJSONFileName) {
        return inputJSONFileName;
    }

    JSONObject json(String jsonFileName) throws IOException, ParseException {
        return (JSONObject) new JSONParser().parse(new FileReader(jsonFileName));
    }

    JSONObject json(HttpResponse httpResponse) throws IOException {
        return (JSONObject) JSONValue.parse(EntityUtils.toString(httpResponse.getEntity()));
    }

    Integer httpStatusCode(HttpResponse httpResponse) {
        return httpResponse.getStatusLine().getStatusCode();
    }

    boolean isGet() {
        return this.methodType.equalsIgnoreCase("GET");
    }

    boolean isPost() {
        return this.methodType.equalsIgnoreCase("POST");
    }

    //boolean isDelete() { return this.methodType.equalsIgnoreCase("DELETE"); }

    void executionCheck() {
        if (execute.equalsIgnoreCase("N"))
            throw new SkipException("Skipping - " + testScriptName + ". The executionFlag set to 'N'");
    }
}
