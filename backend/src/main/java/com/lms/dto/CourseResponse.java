package com.lms.dto;

import com.lms.entity.Course;
import java.time.LocalDateTime;

public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private Long teacherId;
    private String teacherName;
    private LocalDateTime createdAt;
    private QuizResponse quiz;
    private EnrollmentInfo enrollment;

    // Constructors
    public CourseResponse() {}

    public CourseResponse(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.teacherId = course.getTeacher().getId();
        this.teacherName = course.getTeacher().getName();
        this.createdAt = course.getCreatedAt();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public QuizResponse getQuiz() { return quiz; }
    public void setQuiz(QuizResponse quiz) { this.quiz = quiz; }

    public EnrollmentInfo getEnrollment() { return enrollment; }
    public void setEnrollment(EnrollmentInfo enrollment) { this.enrollment = enrollment; }

    public static class EnrollmentInfo {
        private Long id;
        private LocalDateTime enrolledAt;
        private Boolean completed;
        private Integer score;

        // Constructors
        public EnrollmentInfo() {}

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public LocalDateTime getEnrolledAt() { return enrolledAt; }
        public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

        public Boolean getCompleted() { return completed; }
        public void setCompleted(Boolean completed) { this.completed = completed; }

        public Integer getScore() { return score; }
        public void setScore(Integer score) { this.score = score; }
    }
}