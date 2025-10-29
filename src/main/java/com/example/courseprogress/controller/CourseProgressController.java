package com.example.courseprogress.controller;

import com.example.courseprogress.dto.CourseAnalysisResponse;
import com.example.courseprogress.dto.CourseProgressEventRequest;
import com.example.courseprogress.model.CourseProgressEvent;
import com.example.courseprogress.service.CourseProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class CourseProgressController {

    @Autowired
    private CourseProgressService courseProgressService;

    @PostMapping("/events")
    public ResponseEntity<CourseProgressEvent> createEvent(
            @Valid @RequestBody CourseProgressEventRequest request) {
        CourseProgressEvent event = courseProgressService.saveEvent(request);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/events/user/{userId}")
    public ResponseEntity<List<CourseProgressEvent>> getEventsByUser(
            @PathVariable String userId) {
        List<CourseProgressEvent> events = courseProgressService.getEventsByUser(userId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/analysis/course/{courseId}")
    public ResponseEntity<CourseAnalysisResponse> analyzeCourse(
            @PathVariable String courseId) {
        CourseAnalysisResponse analysis = courseProgressService.analyzeCourse(courseId);
        return ResponseEntity.ok(analysis);
    }

    // Test endpoint
    @GetMapping("/test")
    public String test() {
        return "Application is working! Time: " + java.time.LocalDateTime.now();
    }
}