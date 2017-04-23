package com;


import org.testng.annotations.DataProvider;

import java.io.IOException;

import static java.lang.System.arraycopy;

public class TestDataProvider {

    @DataProvider
    static Object[][] dataProvider() throws IOException {
        new DataReader().readCompleteExcel();
        String[][] result = new DataReader().readExcel(0);
        final int noOfRows = DataReader.rowSize;
        final int noOfColumns = DataReader.columnSize;
        Object[][] copy = new Object[noOfRows][noOfColumns];
        for (int row = 0; row < noOfRows; row++) {
            arraycopy(result[row], 0, copy[row], 0, noOfColumns);
        }

        return copy;
    }
}