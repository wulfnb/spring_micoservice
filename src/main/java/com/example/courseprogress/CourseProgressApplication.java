package com.example.courseprogress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseProgressApplication {

    private CourseProgressApplication() {
        // Utility class constructor
    }
    
    public static void main(String[] args) {
        SpringApplication.run(CourseProgressApplication.class, args);
    }
}
