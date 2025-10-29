package com.example.courseprogress.service;

import com.example.courseprogress.dto.CourseAnalysisResponse;
import com.example.courseprogress.dto.CourseProgressEventRequest;
import com.example.courseprogress.model.CourseProgressEvent;
import com.example.courseprogress.model.EventType;
import com.example.courseprogress.repository.CourseProgressEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseProgressServiceTest {

    @Mock
    private CourseProgressEventRepository eventRepository;

    @InjectMocks
    private CourseProgressService courseProgressService;

    private CourseProgressEventRequest validRequest;
    private CourseProgressEvent savedEvent;

    @BeforeEach
    void setUp() {
        validRequest = new CourseProgressEventRequest();
        validRequest.setUserId("user123");
        validRequest.setCourseId("course456");
        validRequest.setTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
        validRequest.setEventType(EventType.COURSE_STARTED);

        savedEvent = new CourseProgressEvent();
        savedEvent.setEventId(UUID.randomUUID());
        savedEvent.setUserId("user123");
        savedEvent.setCourseId("course456");
        savedEvent.setTimestamp(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
        savedEvent.setEventType(EventType.COURSE_STARTED);
    }

    @Test
    void saveEvent_ValidRequest_ReturnsSavedEvent() {
        // Arrange
        when(eventRepository.save(any(CourseProgressEvent.class))).thenReturn(savedEvent);

        // Act
        CourseProgressEvent result = courseProgressService.saveEvent(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals(savedEvent.getEventId(), result.getEventId());
        assertEquals(validRequest.getUserId(), result.getUserId());
        assertEquals(validRequest.getCourseId(), result.getCourseId());
        assertEquals(validRequest.getEventType(), result.getEventType());
        
        verify(eventRepository, times(1)).save(any(CourseProgressEvent.class));
    }

    @Test
    void getEventsByUser_ExistingUser_ReturnsOrderedEvents() {
        // Arrange
        String userId = "user123";
        List<CourseProgressEvent> expectedEvents = Arrays.asList(
            createEvent("user123", "course456", LocalDateTime.of(2024, 1, 15, 10, 0, 0), EventType.COURSE_STARTED),
            createEvent("user123", "course456", LocalDateTime.of(2024, 1, 20, 14, 0, 0), EventType.COURSE_PASSED)
        );
        
        when(eventRepository.findByUserIdOrderByTimestampAsc(userId)).thenReturn(expectedEvents);

        // Act
        List<CourseProgressEvent> result = courseProgressService.getEventsByUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getTimestamp().isBefore(result.get(1).getTimestamp()));
        verify(eventRepository, times(1)).findByUserIdOrderByTimestampAsc(userId);
    }

    @Test
    void analyzeCourse_WithMixedResults_ReturnsCorrectAnalysis() {
        // Arrange
        String courseId = "course456";
        Set<String> startedUsers = new HashSet<>(Arrays.asList("user1", "user2", "user3"));
        Set<String> passedUsers = new HashSet<>(Arrays.asList("user1", "user2"));
        Set<String> failedUsers = new HashSet<>(Collections.singletonList("user3"));

        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_STARTED))
            .thenReturn(startedUsers);
        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_PASSED))
            .thenReturn(passedUsers);
        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_FAILED))
            .thenReturn(failedUsers);

        // Act
        CourseAnalysisResponse result = courseProgressService.analyzeCourse(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(courseId, result.getCourseId());
        assertEquals(3, result.getParticipantsStarted());
        assertEquals(2, result.getParticipantsPassed());
        assertEquals(1, result.getParticipantsFailed());
        assertEquals(66.67, result.getPassRate(), 0.01); // 2/3 = 66.67%
        
        verify(eventRepository, times(1)).findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_STARTED);
        verify(eventRepository, times(1)).findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_PASSED);
        verify(eventRepository, times(1)).findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_FAILED);
    }

    @Test
    void analyzeCourse_NoCompletions_HandlesDivisionByZero() {
        // Arrange
        String courseId = "course456";
        Set<String> startedUsers = new HashSet<>(Arrays.asList("user1", "user2"));
        Set<String> passedUsers = new HashSet<>();
        Set<String> failedUsers = new HashSet<>();

        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_STARTED))
            .thenReturn(startedUsers);
        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_PASSED))
            .thenReturn(passedUsers);
        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_FAILED))
            .thenReturn(failedUsers);

        // Act
        CourseAnalysisResponse result = courseProgressService.analyzeCourse(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getParticipantsStarted());
        assertEquals(0, result.getParticipantsPassed());
        assertEquals(0, result.getParticipantsFailed());
        assertEquals(0.0, result.getPassRate()); // Should handle division by zero gracefully
    }

    @Test
    void analyzeCourse_OnlyFailures_ReturnsZeroPassRate() {
        // Arrange
        String courseId = "course456";
        Set<String> startedUsers = new HashSet<>(Arrays.asList("user1", "user2"));
        Set<String> passedUsers = new HashSet<>();
        Set<String> failedUsers = new HashSet<>(Arrays.asList("user1", "user2"));

        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_STARTED))
            .thenReturn(startedUsers);
        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_PASSED))
            .thenReturn(passedUsers);
        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_FAILED))
            .thenReturn(failedUsers);

        // Act
        CourseAnalysisResponse result = courseProgressService.analyzeCourse(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getParticipantsStarted());
        assertEquals(0, result.getParticipantsPassed());
        assertEquals(2, result.getParticipantsFailed());
        assertEquals(0.0, result.getPassRate()); // 0/2 = 0%
    }

    @Test
    void analyzeCourse_OnlyPasses_ReturnsHundredPercentPassRate() {
        // Arrange
        String courseId = "course456";
        Set<String> startedUsers = new HashSet<>(Arrays.asList("user1", "user2"));
        Set<String> passedUsers = new HashSet<>(Arrays.asList("user1", "user2"));
        Set<String> failedUsers = new HashSet<>();

        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_STARTED))
            .thenReturn(startedUsers);
        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_PASSED))
            .thenReturn(passedUsers);
        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_FAILED))
            .thenReturn(failedUsers);

        // Act
        CourseAnalysisResponse result = courseProgressService.analyzeCourse(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getParticipantsStarted());
        assertEquals(2, result.getParticipantsPassed());
        assertEquals(0, result.getParticipantsFailed());
        assertEquals(100.0, result.getPassRate()); // 2/2 = 100%
    }

    @Test
    void analyzeCourse_NoUsers_ReturnsZeroForAllMetrics() {
        // Arrange
        String courseId = "course456";
        Set<String> emptySet = new HashSet<>();

        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_STARTED))
            .thenReturn(emptySet);
        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_PASSED))
            .thenReturn(emptySet);
        when(eventRepository.findUniqueUsersByCourseIdAndEventType(courseId, EventType.COURSE_FAILED))
            .thenReturn(emptySet);

        // Act
        CourseAnalysisResponse result = courseProgressService.analyzeCourse(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getParticipantsStarted());
        assertEquals(0, result.getParticipantsPassed());
        assertEquals(0, result.getParticipantsFailed());
        assertEquals(0.0, result.getPassRate());
    }

    private CourseProgressEvent createEvent(String userId, String courseId, 
            LocalDateTime timestamp, EventType eventType) {
        CourseProgressEvent event = new CourseProgressEvent();
        event.setEventId(UUID.randomUUID());
        event.setUserId(userId);
        event.setCourseId(courseId);
        event.setTimestamp(timestamp);
        event.setEventType(eventType);
        return event;
    }
}