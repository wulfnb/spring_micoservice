package com.example.courseprogress.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "course_progress_events")
public class CourseProgressEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID eventId;

    @NotNull(message = "userId is required")
    @Column(nullable = false)
    private String userId;

    @NotNull(message = "courseId is required")
    @Column(nullable = false)
    private String courseId;

    @NotNull(message = "timestamp is required")
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @NotNull(message = "eventType is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    public CourseProgressEvent() {}

    public CourseProgressEvent(String userId, String courseId, LocalDateTime timestamp, EventType eventType) {
        this.userId = userId;
        this.courseId = courseId;
        this.timestamp = timestamp;
        this.eventType = eventType;
    }

    // Getters and Setters
    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }
}
