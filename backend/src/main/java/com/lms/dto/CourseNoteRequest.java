package com.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CourseNoteRequest {
    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    @Size(max = 5000)
    private String content;

    private Integer order;

    // Constructors
    public CourseNoteRequest() {}

    public CourseNoteRequest(String title, String content, Integer order) {
        this.title = title;
        this.content = content;
        this.order = order;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }
}