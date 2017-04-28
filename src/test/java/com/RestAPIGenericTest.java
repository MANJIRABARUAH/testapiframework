package com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.parser.ParseException;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.apache.commons.lang.StringUtils.trimToEmpty;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class RestAPIGenericTest extends RestAPIAbstractTest {

    private static final Log LOG = LogFactory.getLog(RestAPIGenericTest.class);

    @BeforeMethod
    public void setUpTest() {
        httpClient = HttpClientBuilder.create().build();
    }

    @UseAsTestName()
    @Test(
            dataProvider = "dataProvider",
            dataProviderClass = TestDataProvider.class
    )
    public void restAPITestFor(String testScriptName,
                               String methodType,
                               String parametrisedURL,
                               String headerJSONFileName,
                               String inputJSONFileName,
                               String expectedResponseCode,
                               String expectedJSONFileName,
                               String execute,
                               String testDescription) {
        super.testScriptName = trimToEmpty(testScriptName);
        super.methodType = trimToEmpty(methodType);
        super.parametrisedURL = trimToEmpty(parametrisedURL);
        super.expectedResponseCode = trimToEmpty(expectedResponseCode);
        super.execute = trimToEmpty(execute);
        super.testDescription = trimToEmpty(testDescription);
        super.headerJSONFileName = trimToEmpty(headerJSONFileName).isEmpty() ? "" : PropertyFileReader.HEADERJSON + trimToEmpty(headerJSONFileName);
        super.inputJSONFileName = trimToEmpty(inputJSONFileName).isEmpty() ? "" : PropertyFileReader.INPUTJSON + trimToEmpty(inputJSONFileName);
        super.expectedJSONFileName = trimToEmpty(expectedJSONFileName).isEmpty() ? "" : PropertyFileReader.RESULTJSON + trimToEmpty(expectedJSONFileName);
        //    ctx.setAttribute("testName", "MyTestName");
        Reporter.log(testScriptName + " :- " + testDescription);
        try {
            executionCheck();
            if (isGet()) testHttpClient_GET();
            else if (isPost()) testHttpClient_POST();
            //else if (isDelete()) testHttpClient_DELETE();

        } catch (URISyntaxException | IOException | ParseException ex) {
            LOG.info("Skipping - " + this.testScriptName + " Cause: " + ex.getMessage());
            throw new SkipException("Skipping - " + this.testScriptName + " Cause: " + ex.getMessage());
        }
    }

    private void testHttpClient_POST() throws IOException, URISyntaxException, ParseException {
        HttpResponse httpResponse = requestPOST();
        assertHttpResponse(httpResponse);
    }

    private void testHttpClient_GET() throws IOException, URISyntaxException, ParseException {
        HttpResponse httpResponse = requestGET();
        assertHttpResponse(httpResponse);

    }

    private void assertHttpResponse(HttpResponse httpResponse) throws IOException, ParseException {
        Reporter.log(" <br/>1. Availability check for REST API > " + parametrisedURL + ", HTTP Method " + methodType);
        customAssertNotNull(httpResponse, "\" no response; something went wrong, check logs\"");

        Reporter.log("<br/>2. Check for HTTP StatusCode > " + "Actual: " + httpStatusCode(httpResponse) + ", Expected: " + expectedResponseCode);
        customAssertEquals(httpStatusCode(httpResponse), new Integer(expectedResponseCode), " HTTPResponse code mismatch! " + httpResponse.getEntity().getContent());

        if (!super.expectedJSONFileName.isEmpty()) {
            Reporter.log("<br/>3. Validate the Response");
            customAssertEquals(json(httpResponse), json(expectedJSONFileName), "response content mismatch");
        }
    }

    private void customAssertNotNull(HttpResponse httpResponse, String msg) {
        try {
            assertNotNull(httpResponse, msg);
            Reporter.log(" -  <span style=\"color:green;\">Pass</span>");
        } catch (AssertionError ex) {
            Reporter.log(" -  <span style=\"color:red;\">Fail</span>");
            throw ex;
        }
    }

    private void customAssertEquals(Object actual, Object expected, String msg) {
        try {
            assertEquals(actual, expected, msg);
            Reporter.log(" - <span style=\"color:green;\">Pass</span>");
        } catch (AssertionError ex) {
            Reporter.log(" -  <span style=\"color:red;\">Fail</span>");
            throw ex;
        }
    }

    @AfterMethod
    public void TearDown() {
        try {
            httpClient.close();
        } catch (IOException ex) {
            LOG.error("Error closing HttpClient!");
        }
    }

    @AfterClass
    public static void TearDownTest() {
    }


}
