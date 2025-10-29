package com.example.courseprogress.util;

import java.util.*;
import java.io.*;  // Checkstyle: unused import

public class StyleViolation {
    
    private String bad_naming;  // Checkstyle: variable naming
    
    public void methodWithIssues()
    {  // Checkstyle: brace placement
        System.out.println("bad indentation");  // Checkstyle: indentation
    }
    
    // Checkstyle: missing javadoc
    public void undocumentedMethod() {
        int TOO_LONG_VARIABLE_NAME_THAT_EXCEEDS_TYPICAL_LENGTH_LIMITS = 1;  // Checkstyle: line length
    }
}
