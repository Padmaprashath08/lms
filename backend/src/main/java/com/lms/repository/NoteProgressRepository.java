package com.lms.repository;

import com.lms.entity.NoteProgress;
import com.lms.entity.User;
import com.lms.entity.CourseNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteProgressRepository extends JpaRepository<NoteProgress, Long> {
    List<NoteProgress> findByStudent(User student);
    List<NoteProgress> findByStudentId(Long studentId);
    List<NoteProgress> findByCourseNote(CourseNote courseNote);
    Optional<NoteProgress> findByStudentAndCourseNote(User student, CourseNote courseNote);
    boolean existsByStudentIdAndCourseNoteId(Long studentId, Long courseNoteId);
    long countByStudentIdAndCourseNote_CourseId(Long studentId, Long courseId);
    
    @Query("SELECT np FROM NoteProgress np WHERE np.student.id = :studentId AND np.courseNote.course.id = :courseId")
    List<NoteProgress> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(np) FROM NoteProgress np WHERE np.student.id = :studentId AND np.courseNote.course.id = :courseId")
    Long countByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(cn) FROM CourseNote cn WHERE cn.course.id = :courseId")
    Long countCourseNotesByCourseId(@Param("courseId") Long courseId);
}