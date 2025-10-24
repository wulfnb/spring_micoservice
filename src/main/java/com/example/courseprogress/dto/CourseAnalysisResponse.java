package com.example.courseprogress.dto;

public class CourseAnalysisResponse {
    private String courseId;
    private long participantsStarted;
    private long participantsPassed;
    private long participantsFailed;
    private double passRate;

    public CourseAnalysisResponse() {}

    public CourseAnalysisResponse(String courseId, long participantsStarted, 
                                 long participantsPassed, long participantsFailed, 
                                 double passRate) {
        this.courseId = courseId;
        this.participantsStarted = participantsStarted;
        this.participantsPassed = participantsPassed;
        this.participantsFailed = participantsFailed;
        this.passRate = passRate;
    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public long getParticipantsStarted() { return participantsStarted; }
    public void setParticipantsStarted(long participantsStarted) { this.participantsStarted = participantsStarted; }

    public long getParticipantsPassed() { return participantsPassed; }
    public void setParticipantsPassed(long participantsPassed) { this.participantsPassed = participantsPassed; }

    public long getParticipantsFailed() { return participantsFailed; }
    public void setParticipantsFailed(long participantsFailed) { this.participantsFailed = participantsFailed; }

    public double getPassRate() { return passRate; }
    public void setPassRate(double passRate) { this.passRate = passRate; }
}