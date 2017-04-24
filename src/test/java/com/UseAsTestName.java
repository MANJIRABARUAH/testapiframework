package com;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface UseAsTestName {

    /**
     * Index of the parameter to use as the Test Case ID.
     */
    int idx() default 0;
}
