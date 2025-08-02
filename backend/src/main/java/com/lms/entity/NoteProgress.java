package com.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "note_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_note_id"})
})
public class NoteProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_note_id", nullable = false)
    private CourseNote courseNote;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    // Constructors
    public NoteProgress() {
        this.readAt = LocalDateTime.now();
    }

    public NoteProgress(User student, CourseNote courseNote) {
        this.student = student;
        this.courseNote = courseNote;
        this.readAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public CourseNote getCourseNote() { return courseNote; }
    public void setCourseNote(CourseNote courseNote) { this.courseNote = courseNote; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
}