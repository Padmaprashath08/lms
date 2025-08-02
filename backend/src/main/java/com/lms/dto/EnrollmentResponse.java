package com.lms.dto;

import com.lms.entity.Enrollment;
import java.time.LocalDateTime;

public class EnrollmentResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long courseId;
    private String courseTitle;
    private LocalDateTime enrolledAt;
    private Boolean completed;
    private Integer score;

    // Constructors
    public EnrollmentResponse() {}

    public EnrollmentResponse(Enrollment enrollment) {
        this.id = enrollment.getId();
        this.studentId = enrollment.getStudent().getId();
        this.studentName = enrollment.getStudent().getName();
        this.studentEmail = enrollment.getStudent().getEmail();
        this.courseId = enrollment.getCourse().getId();
        this.courseTitle = enrollment.getCourse().getTitle();
        this.enrolledAt = enrollment.getEnrolledAt();
        this.completed = enrollment.getCompleted();
        this.score = enrollment.getScore();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}