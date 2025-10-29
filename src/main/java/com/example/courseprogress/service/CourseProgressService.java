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
        Set<String> startedUsers = getUsersByEventType(courseId, EventType.COURSE_STARTED);
        Set<String> passedUsers = getUsersByEventType(courseId, EventType.COURSE_PASSED);
        Set<String> failedUsers = getUsersByEventType(courseId, EventType.COURSE_FAILED);

        double passRate = calculatePassRate(passedUsers, failedUsers);

        return createAnalysisResponse(courseId, startedUsers, passedUsers, failedUsers, passRate);
    }

    private Set<String> getUsersByEventType(String courseId, EventType eventType) {
        return eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, eventType);
    }

    private double calculatePassRate(Set<String> passedUsers, Set<String> failedUsers) {
        long totalCompleted = passedUsers.size() + failedUsers.size();
        if (totalCompleted == 0) {
            return 0.0;
        }
        double percentage = (double) passedUsers.size() / totalCompleted * 100;
        return Math.round(percentage * 100.0) / 100.0;
    }

    private CourseAnalysisResponse createAnalysisResponse(String courseId,
            Set<String> startedUsers,
            Set<String> passedUsers,
            Set<String> failedUsers,
            double passRate) {
        return new CourseAnalysisResponse(
                courseId,
                startedUsers.size(),
                passedUsers.size(),
                failedUsers.size(),
                passRate);
    }
}