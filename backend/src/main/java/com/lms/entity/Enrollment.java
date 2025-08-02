package com.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;

    @Column(name = "completed")
    private Boolean completed = false;

    @Column(name = "score")
    private Integer score;

    // Constructors
    public Enrollment() {
        this.enrolledAt = LocalDateTime.now();
    }

    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
        this.enrolledAt = LocalDateTime.now();
        this.completed = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}