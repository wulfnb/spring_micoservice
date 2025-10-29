package com.example.courseprogress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class CourseProgressApplication {
    
    // Private constructor to prevent instantiation
    private CourseProgressApplication() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void main(String[] args) {
        SpringApplication.run(CourseProgressApplication.class, args);
    }
}