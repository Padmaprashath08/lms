package com.lms.dto;

import com.lms.entity.Question;
import java.util.List;

public class QuestionResponse {
    private Long id;
    private String question;
    private List<String> options;
    private Integer correctAnswer;

    // Constructors
    public QuestionResponse() {}

    public QuestionResponse(Question question) {
        this.id = question.getId();
        this.question = question.getQuestion();
        this.options = question.getOptions();
        this.correctAnswer = question.getCorrectAnswer();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public Integer getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(Integer correctAnswer) { this.correctAnswer = correctAnswer; }
}