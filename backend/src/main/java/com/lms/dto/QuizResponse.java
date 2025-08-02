package com.lms.dto;

import com.lms.entity.Quiz;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class QuizResponse {
    private Long id;
    private String title;
    private Long courseId;
    private LocalDateTime createdAt;
    private List<QuestionResponse> questions;

    // Constructors
    public QuizResponse() {}

    public QuizResponse(Quiz quiz) {
        this.id = quiz.getId();
        this.title = quiz.getTitle();
        this.courseId = quiz.getCourse().getId();
        this.createdAt = quiz.getCreatedAt();
        this.questions = quiz.getQuestions().stream()
                .map(QuestionResponse::new)
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<QuestionResponse> getQuestions() { return questions; }
    public void setQuestions(List<QuestionResponse> questions) { this.questions = questions; }
}