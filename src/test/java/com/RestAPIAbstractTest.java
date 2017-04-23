package com;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.SkipException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

abstract class RestAPIAbstractTest {

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
        JsonMapper paramMapper = new JsonMapper(new File(getFilePath(inputJSONFileName)));
        paramMapper.getDataMapper().forEach(builder::setParameter);
        HttpGet getRequest = new HttpGet(builder.build());
        JsonMapper headerMapper = new JsonMapper(new File(getFilePath(headerJSONFileName)));
        headerMapper.getDataMapper().forEach(getRequest::addHeader);
        return httpClient.execute(getRequest);
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

    boolean isDelete() {
        return this.methodType.equalsIgnoreCase("DELETE");
    }

    void executionCheck() {
        if (execute.equalsIgnoreCase("N"))
            throw new SkipException("Skipping - " + testScriptName + ". The executionFlag set to 'N'");
    }
}
