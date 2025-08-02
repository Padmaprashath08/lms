package com.lms.repository;

import com.lms.entity.CourseNote;
import com.lms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseNoteRepository extends JpaRepository<CourseNote, Long> {
    List<CourseNote> findByCourseOrderByOrderAsc(Course course);
    List<CourseNote> findByCourseIdOrderByOrderAsc(Long courseId);
    long countByCourseId(Long courseId);
    void deleteByCourseId(Long courseId);
}