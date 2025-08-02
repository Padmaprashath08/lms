package com.lms.repository;

import com.lms.entity.QuizSubmission;
import com.lms.entity.User;
import com.lms.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    List<QuizSubmission> findByStudent(User student);
    List<QuizSubmission> findByQuiz(Quiz quiz);
    Optional<QuizSubmission> findByStudentAndQuiz(User student, Quiz quiz);
    boolean existsByStudentIdAndQuizId(Long studentId, Long quizId);
}