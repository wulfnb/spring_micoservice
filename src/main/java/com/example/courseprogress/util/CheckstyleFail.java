package com.example.courseprogress.util;

// Unused imports - will fail
import java.util.ArrayList;
import java.util.List;

public class CheckstyleFail {
    
    // Wrong naming convention - will fail
    private String BadVariableName;
    
    // Line way too long - will definitely fail
    public void methodWithExtremelyLongNameThatViolatesCheckstyleLineLengthRulesBecauseItExceedsTheMaximumAllowedCharacters() {
        System.out.println("This is an extremely long line that should definitely fail checkstyle because it exceeds the maximum line length limit by a significant margin");
    }
    
    // Missing javadoc - will fail
    public void test() {
        // Bad indentation - will fail
          System.out.println("wrong indentation");
    }
    
    // Empty catch block - will fail
    public void emptyCatch() {
        try {
            int x = 1 / 0;
        } catch (Exception e) {
            // empty
        }
    }
}
