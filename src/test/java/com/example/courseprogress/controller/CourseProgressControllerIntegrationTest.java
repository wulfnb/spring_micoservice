package com.example.courseprogress.controller;

import com.example.courseprogress.model.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class CourseProgressControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Clean database or setup initial data if needed
    }

    @Test
    void createEvent_ValidRequest_ReturnsCreatedEvent() throws Exception {
        // Arrange
        String eventJson = "{" +
            "\"userId\": \"user123\"," +
            "\"courseId\": \"course456\"," +
            "\"timestamp\": \"2024-01-15T10:30:00\"," +
            "\"eventType\": \"COURSE_STARTED\"" +
            "}";

        // Act & Assert
        mockMvc.perform(post("/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", notNullValue()))
                .andExpect(jsonPath("$.userId", is("user123")))
                .andExpect(jsonPath("$.courseId", is("course456")))
                .andExpect(jsonPath("$.eventType", is("COURSE_STARTED")));
    }

    @Test
    void createEvent_MissingRequiredFields_ReturnsBadRequest() throws Exception {
        // Arrange
        String invalidEventJson = "{" +
            "\"userId\": \"user123\"," +
            "\"timestamp\": \"2024-01-15T10:30:00\"" +
            "}";

        // Act & Assert
        mockMvc.perform(post("/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidEventJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getEventsByUser_UserWithEvents_ReturnsEventList() throws Exception {
        // Arrange - First create an event
        String eventJson = "{" +
            "\"userId\": \"testUser\"," +
            "\"courseId\": \"testCourse\"," +
            "\"timestamp\": \"2024-01-15T10:30:00\"," +
            "\"eventType\": \"COURSE_STARTED\"" +
            "}";

        mockMvc.perform(post("/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson));

        // Act & Assert
        mockMvc.perform(get("/v1/events/user/testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId", is("testUser")))
                .andExpect(jsonPath("$[0].courseId", is("testCourse")));
    }

    @Test
    void getEventsByUser_UserWithoutEvents_ReturnsEmptyList() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/events/user/nonexistentUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    void analyzeCourse_WithCourseEvents_ReturnsCorrectAnalysis() throws Exception {
        // Arrange - Create multiple events for analysis
        createTestEvent("user1", "analysisCourse", EventType.COURSE_STARTED);
        createTestEvent("user1", "analysisCourse", EventType.COURSE_PASSED);
        createTestEvent("user2", "analysisCourse", EventType.COURSE_STARTED);
        createTestEvent("user2", "analysisCourse", EventType.COURSE_FAILED);
        createTestEvent("user3", "analysisCourse", EventType.COURSE_STARTED);

        // Act & Assert
        mockMvc.perform(get("/v1/analysis/course/analysisCourse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId", is("analysisCourse")))
                .andExpect(jsonPath("$.participantsStarted", is(3)))
                .andExpect(jsonPath("$.participantsPassed", is(1)))
                .andExpect(jsonPath("$.participantsFailed", is(1)))
                .andExpect(jsonPath("$.passRate", is(50.0))); // 1 passed / (1 passed + 1 failed) = 50%
    }

    @Test
    void analyzeCourse_NoEvents_ReturnsZeroMetrics() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/analysis/course/nonexistentCourse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId", is("nonexistentCourse")))
                .andExpect(jsonPath("$.participantsStarted", is(0)))
                .andExpect(jsonPath("$.participantsPassed", is(0)))
                .andExpect(jsonPath("$.participantsFailed", is(0)))
                .andExpect(jsonPath("$.passRate", is(0.0)));
    }

    @Test
    void analyzeCourse_OnlyStartedEvents_ReturnsZeroPassRate() throws Exception {
        // Arrange
        createTestEvent("user1", "startedOnlyCourse", EventType.COURSE_STARTED);
        createTestEvent("user2", "startedOnlyCourse", EventType.COURSE_STARTED);

        // Act & Assert
        mockMvc.perform(get("/v1/analysis/course/startedOnlyCourse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantsStarted", is(2)))
                .andExpect(jsonPath("$.participantsPassed", is(0)))
                .andExpect(jsonPath("$.participantsFailed", is(0)))
                .andExpect(jsonPath("$.passRate", is(0.0)));
    }

    private void createTestEvent(String userId, String courseId, EventType eventType) throws Exception {
        String eventJson = "{" +
            "\"userId\": \"" + userId + "\"," +
            "\"courseId\": \"" + courseId + "\"," +
            "\"timestamp\": \"2024-01-15T10:30:00\"," +
            "\"eventType\": \"" + eventType + "\"" +
            "}";

        mockMvc.perform(post("/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson));
    }
}