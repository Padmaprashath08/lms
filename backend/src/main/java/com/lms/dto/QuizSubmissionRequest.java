package com.lms.dto;

import java.util.List;

public class QuizSubmissionRequest {
    private List<Integer> answers;

    // Constructors
    public QuizSubmissionRequest() {}

    public QuizSubmissionRequest(List<Integer> answers) {
        this.answers = answers;
    }

    // Getters and Setters
    public List<Integer> getAnswers() { return answers; }
    public void setAnswers(List<Integer> answers) { this.answers = answers; }
}