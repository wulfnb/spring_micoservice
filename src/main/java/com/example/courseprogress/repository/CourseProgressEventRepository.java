package com.example.courseprogress.repository;

import com.example.courseprogress.model.CourseProgressEvent;
import com.example.courseprogress.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CourseProgressEventRepository extends JpaRepository<CourseProgressEvent, String> {

    List<CourseProgressEvent> findByUserIdOrderByTimestampAsc(String userId);

    @Query("SELECT DISTINCT e.userId FROM CourseProgressEvent e " +
       "WHERE e.courseId = :courseId AND e.eventType = :eventType")
    Set<String> findUniqueUsersByCourseIdAndEventType(@Param("courseId") String courseId, 
                                                     @Param("eventType") EventType eventType);

    @Query("SELECT DISTINCT e.userId FROM CourseProgressEvent e WHERE e.courseId = :courseId")
    Set<String> findUniqueUsersByCourseId(@Param("courseId") String courseId);
}