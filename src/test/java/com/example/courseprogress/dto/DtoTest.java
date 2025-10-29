package com.example.courseprogress.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DtoTest {

    @Test
    void courseAnalysisResponse_ConstructorAndSetters_WorkCorrectly() {
        // Test constructor with parameters
        CourseAnalysisResponse response = new CourseAnalysisResponse(
            "course123", 10, 8, 2, 80.0
        );
        
        assertEquals("course123", response.getCourseId());
        assertEquals(10, response.getParticipantsStarted());
        assertEquals(8, response.getParticipantsPassed());
        assertEquals(2, response.getParticipantsFailed());
        assertEquals(80.0, response.getPassRate());
        
        // Test default constructor and setters
        CourseAnalysisResponse response2 = new CourseAnalysisResponse();
        response2.setCourseId("course456");
        response2.setParticipantsStarted(5);
        response2.setParticipantsPassed(4);
        response2.setParticipantsFailed(1);
        response2.setPassRate(80.0);
        
        assertEquals("course456", response2.getCourseId());
        assertEquals(5, response2.getParticipantsStarted());
        assertEquals(4, response2.getParticipantsPassed());
        assertEquals(1, response2.getParticipantsFailed());
        assertEquals(80.0, response2.getPassRate());
    }

    @Test
    void courseProgressEventRequest_ConstructorAndSetters_WorkCorrectly() {
        // Test default constructor and setters
        CourseProgressEventRequest request = new CourseProgressEventRequest();
        request.setUserId("testUser");
        request.setCourseId("testCourse");
        request.setTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
        request.setEventType(com.example.courseprogress.model.EventType.COURSE_STARTED);
        
        assertEquals("testUser", request.getUserId());
        assertEquals("testCourse", request.getCourseId());
        assertNotNull(request.getTimestamp());
        assertEquals(com.example.courseprogress.model.EventType.COURSE_STARTED, request.getEventType());
    }

    @Test
    void courseAnalysisResponse_ToString_ContainsRelevantInfo() {
        CourseAnalysisResponse response = new CourseAnalysisResponse("course1", 10, 8, 2, 80.0);
        String toString = response.toString();
        
        assertNotNull(toString);
        // Should contain class name
        assertTrue(toString.contains("CourseAnalysisResponse"));
    }

    @Test
    void courseProgressEventRequest_ToString_ContainsRelevantInfo() {
        CourseProgressEventRequest request = new CourseProgressEventRequest();
        request.setUserId("user1");
        request.setCourseId("course1");
        request.setTimestamp(LocalDateTime.now());
        request.setEventType(com.example.courseprogress.model.EventType.COURSE_STARTED);
        
        String toString = request.toString();
        assertNotNull(toString);
        // Should contain class name
        assertTrue(toString.contains("CourseProgressEventRequest"));
    }
}