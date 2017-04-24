package com;

import org.testng.ITest;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class UseAsTestName_TestBase implements ITest {

    private String testInstanceName = "";

    private void setTestName(String anInstanceName) {
        testInstanceName = "test_" + anInstanceName;
    }

    public String getTestName() {
        return testInstanceName;
    }

    @BeforeMethod(alwaysRun = true)
    public void extractTestNameFromParameters(Method method, Object[] parameters) {
        checkNotNull(method);
        checkNotNull(parameters);
        setTestName(method.getName());
        UseAsTestName useAsTestName = method.getAnnotation(UseAsTestName.class);
        if (useAsTestName != null) {
            if (useAsTestName.idx() > parameters.length - 1) {
                throw new IllegalArgumentException("Incorrect parameter as a Test Case ID");
            }
            Object parmAsObj = parameters[useAsTestName.idx()];
            if (!String.class.isAssignableFrom(parmAsObj.getClass())) {
                throw new IllegalArgumentException("Incorrect parameter type for  Test Case ID");
            }
            String testCaseId = (String) parameters[useAsTestName.idx()];
            setTestName(testCaseId);
        }
    }
}
