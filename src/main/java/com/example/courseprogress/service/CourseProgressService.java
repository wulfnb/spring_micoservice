package com.example.courseprogress.service;

import com.example.courseprogress.dto.CourseAnalysisResponse;
import com.example.courseprogress.dto.CourseProgressEventRequest;
import com.example.courseprogress.model.CourseProgressEvent;
import com.example.courseprogress.model.EventType;
import com.example.courseprogress.repository.CourseProgressEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CourseProgressService {

    @Autowired
    private CourseProgressEventRepository eventRepository;

    public CourseProgressEvent saveEvent(CourseProgressEventRequest request) {
        CourseProgressEvent event = new CourseProgressEvent();
        event.setUserId(request.getUserId());
        event.setCourseId(request.getCourseId());
        event.setTimestamp(request.getTimestamp());
        event.setEventType(request.getEventType());
        
        return eventRepository.save(event);
    }

    public List<CourseProgressEvent> getEventsByUser(String userId) {
        return eventRepository.findByUserIdOrderByTimestampAsc(userId);
    }

    public CourseAnalysisResponse analyzeCourse(String courseId) {
        // Get unique users who started the course
        Set<String> startedUsers = eventRepository.findUniqueUsersByCourseIdAndEventType(
            courseId, EventType.COURSE_STARTED);
        
        // Get unique users who passed the course
        Set<String> passedUsers = eventRepository.findUniqueUsersByCourseIdAndEventType(
            courseId, EventType.COURSE_PASSED);
        
        // Get unique users who failed the course
        Set<String> failedUsers = eventRepository.findUniqueUsersByCourseIdAndEventType(
            courseId, EventType.COURSE_FAILED);

        // Calculate pass rate (handle division by zero)
        double passRate = 0.0;
        long totalCompleted = passedUsers.size() + failedUsers.size();
        if (totalCompleted > 0) {
            passRate = (double) passedUsers.size() / totalCompleted * 100;
            passRate = Math.round(passRate * 100.0) / 100.0; // Round to 2 decimal places
        }

        return new CourseAnalysisResponse(
            courseId,
            startedUsers.size(),
            passedUsers.size(),
            failedUsers.size(),
            passRate
        );
    }
}