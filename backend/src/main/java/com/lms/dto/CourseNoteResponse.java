package com.lms.dto;

import com.lms.entity.CourseNote;
import java.time.LocalDateTime;

public class CourseNoteResponse {
    private Long id;
    private String title;
    private String content;
    private Integer order;
    private Long courseId;
    private LocalDateTime createdAt;
    private boolean isRead;

    // Constructors
    public CourseNoteResponse() {}

    public CourseNoteResponse(CourseNote note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.order = note.getOrder();
        this.isRead = false; // Default value
    }

    public CourseNoteResponse(Long id, String title, String content, int order, boolean isRead) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.order = order;
        this.isRead = isRead;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}