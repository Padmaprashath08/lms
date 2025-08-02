package com.lms.service;

import com.lms.dto.QuizResponse;
import com.lms.dto.QuizSubmissionRequest;
import com.lms.entity.*;
import com.lms.repository.*;
import com.lms.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseNoteService courseNoteService;
    public QuizResponse getQuizByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Check if student is enrolled
        if (!enrollmentRepository.existsByStudentIdAndCourseId(userPrincipal.getId(), courseId)) {
            throw new RuntimeException("Not enrolled in this course");
        }

        // Check if student has completed all notes before allowing quiz access
        if (!courseNoteService.hasCompletedAllNotes(courseId)) {
            throw new RuntimeException("Must complete all course notes before taking quiz");
        }
        Quiz quiz = quizRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Quiz not found for this course"));

        return new QuizResponse(quiz);
    }

    @Transactional
    public Map<String, Object> submitQuiz(Long courseId, QuizSubmissionRequest submissionRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User student = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Quiz quiz = quizRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Quiz not found for this course"));

        // Check if already submitted
        if (quizSubmissionRepository.existsByStudentIdAndQuizId(student.getId(), quiz.getId())) {
            throw new RuntimeException("Quiz already submitted");
        }

        // Double-check that all notes are completed before submission
        if (!courseNoteService.hasCompletedAllNotes(courseId)) {
            throw new RuntimeException("Must complete all course notes before submitting quiz");
        }
        // Calculate score
        List<Question> questions = quiz.getQuestions();
        List<Integer> answers = submissionRequest.getAnswers();
        
        int correctAnswers = 0;
        for (int i = 0; i < questions.size() && i < answers.size(); i++) {
            if (questions.get(i).getCorrectAnswer().equals(answers.get(i))) {
                correctAnswers++;
            }
        }

        int score = questions.size() > 0 ? (correctAnswers * 100) / questions.size() : 0;

        // Save submission
        QuizSubmission submission = new QuizSubmission(student, quiz, answers, score);
        quizSubmissionRepository.save(submission);

        // Update enrollment
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollment.setCompleted(true);
        enrollment.setScore(score);
        enrollmentRepository.save(enrollment);

        Map<String, Object> result = new HashMap<>();
        result.put("score", score);
        result.put("correctAnswers", correctAnswers);
        result.put("totalQuestions", questions.size());

        return result;
    }
}