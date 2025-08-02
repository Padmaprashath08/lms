package com.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CourseRequest {
    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String description;

    private QuizRequest quiz;

    // Constructors
    public CourseRequest() {
    }

    public CourseRequest(String title, String description, QuizRequest quiz) {
        this.title = title;
        this.description = description;
        this.quiz = quiz;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuizRequest getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizRequest quiz) {
        this.quiz = quiz;
    }

    public static class QuizRequest {
        private String title;
        private List<QuestionRequest> questions;

        // Constructors
        public QuizRequest() {
        }

        public QuizRequest(String title, List<QuestionRequest> questions) {
            this.title = title;
            this.questions = questions;
        }

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<QuestionRequest> getQuestions() {
            return questions;
        }

        public void setQuestions(List<QuestionRequest> questions) {
            this.questions = questions;
        }
    }

    public static class QuestionRequest {
        private String question;
        private List<String> options;
        private Integer correctAnswer;
        private Long quizId;
        // Constructors
        public QuestionRequest() {
        }

        public QuestionRequest(String question, List<String> options, Integer correctAnswer) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }

        // Getters and Setters
        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public Integer getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(Integer correctAnswer) {
            this.correctAnswer = correctAnswer;
        }
        public Long getQuizId() { return quizId; }
        public void setQuizId(Long quizId) { this.quizId = quizId; }
    }
}