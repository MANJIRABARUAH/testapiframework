package com;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

final class PropertyFileReader {

    private static Properties prop = new Properties();

    private static final String CONFIG_FILE = "test-config.properties";

    static {
        try {
            prop.load(new FileInputStream(CONFIG_FILE));
        } catch (IOException ex) {
            throw new ConfigPropertiesException("Error reading property file " + CONFIG_FILE, ex);
        }
    }

    static final String TESTDATAFILE = prop.getProperty("testdata.filePath");

    static final String INPUTJSON = prop.getProperty("testdata.filePath.parametersJson");

    static final String HEADERJSON = prop.getProperty("testdata.filePath.headerJson");

    static final String RESULTJSON = prop.getProperty("testdata.filePath.resultJson");

}
