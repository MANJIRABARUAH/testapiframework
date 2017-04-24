package com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.parser.ParseException;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class RestAPIGenericTest extends RestAPIAbstractTest {

    private static final Log LOG = LogFactory.getLog(RestAPIGenericTest.class);


    @BeforeClass
    public static void setUp() throws IOException {

    }


    @BeforeMethod
    public void setUpTest() {
        httpClient = HttpClientBuilder.create().build();
    }

    @Test(
            dataProvider = "dataProvider",
            dataProviderClass = TestDataProvider.class
    )
    public void genericTestForRestAPI(String testScriptName,
                                      String methodType,
                                      String parametrisedURL,
                                      String headerJSONFileName,
                                      String inputJSONFileName,
                                      String expectedResponseCode,
                                      String expectedJSONFileName,
                                      String execute,
                                      String testDescription) {
        super.testScriptName = testScriptName;
        super.methodType = methodType;
        super.parametrisedURL = parametrisedURL;
        super.expectedResponseCode = expectedResponseCode;
        super.execute = execute;
        super.testDescription = testDescription;
        super.headerJSONFileName = PropertyFileReader.HEADERJSON + headerJSONFileName;
        super.inputJSONFileName = PropertyFileReader.INPUTJSON + inputJSONFileName;
        super.expectedJSONFileName = PropertyFileReader.RESULTJSON + expectedJSONFileName;

        Reporter.log(testScriptName + " :- " + testDescription);
        try {
            executionCheck();
            if (isGet()) testHttpClient_GET();
            else if (isPost()) testHttpClient_POST();
            else if (isDelete()) testHttpClient_DELETE();

        } catch (Exception ex) {
            LOG.info("Skipping - " + this.testScriptName + " Cause: " + ex.getMessage());
            throw new SkipException("Skipping - " + this.testScriptName + " Cause: " + ex.getMessage());
        }
    }

    private void testHttpClient_DELETE() {

    }

    private void testHttpClient_POST() {

    }

    private void testHttpClient_GET() throws IOException, URISyntaxException, ParseException {

        HttpResponse httpResponse = requestGET();

        Reporter.log("1. Availability check for REST API > " + parametrisedURL + ", HTTP Method " + methodType + ", Parameters (JSON) " + json(inputJSONFileName));
        assertNotNull(httpResponse, " no response; something went wrong, check logs");

        Reporter.log("2. Check for HTTP StatusCode > " + "Actual: " + httpStatusCode(httpResponse) + ", Expected: " + expectedResponseCode);
        assertEquals(httpStatusCode(httpResponse), new Integer(expectedResponseCode), " HTTPResponse code mismatch! " + httpResponse.getEntity().getContent());

        Reporter.log("3. Validate the Response");
        assertEquals(json(httpResponse), json(expectedJSONFileName), "response content mismatch");

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
