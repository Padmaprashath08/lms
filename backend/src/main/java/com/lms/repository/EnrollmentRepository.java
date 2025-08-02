package com.lms.repository;

import com.lms.entity.Enrollment;
import com.lms.entity.User;
import com.lms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(User student);
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourse(Course course);
    List<Enrollment> findByCourseId(Long courseId);
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}