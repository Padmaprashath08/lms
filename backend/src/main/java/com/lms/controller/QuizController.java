package com.lms.controller;

import com.lms.dto.QuizResponse;
import com.lms.dto.QuizSubmissionRequest;
import com.lms.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<QuizResponse> getQuizByCourseId(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(quizService.getQuizByCourseId(courseId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/course/{courseId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> submitQuiz(@PathVariable Long courseId, @Valid @RequestBody QuizSubmissionRequest submissionRequest) {
        try {
            return ResponseEntity.ok(quizService.submitQuiz(courseId, submissionRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}