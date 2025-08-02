package com.lms.service;

import com.lms.dto.CourseNoteResponse;
import com.lms.dto.EnrollmentResponse;
import com.lms.dto.NotesProgressResponse;
import com.lms.entity.Course;
import com.lms.entity.CourseNote;
import com.lms.entity.Enrollment;
import com.lms.entity.User;
import com.lms.repository.CourseNoteRepository;
import com.lms.repository.CourseRepository;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.NoteProgressRepository;
import com.lms.repository.UserRepository;
import com.lms.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseNoteRepository courseNoteRepository;

    @Autowired
    private NoteProgressRepository noteProgressRepository;

    public EnrollmentResponse enrollInCourse(Long courseId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User student = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if already enrolled
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new RuntimeException("Already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment(student, course);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return new EnrollmentResponse(savedEnrollment);
    }

    public List<EnrollmentResponse> getStudentEnrollments() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return enrollmentRepository.findByStudentId(userPrincipal.getId()).stream()
                .map(EnrollmentResponse::new)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getCourseEnrollments(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!course.getTeacher().getId().equals(userPrincipal.getId())) {
            throw new RuntimeException("Not authorized to view enrollments for this course");
        }

        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(EnrollmentResponse::new)
                .collect(Collectors.toList());
    }

    public NotesProgressResponse getNotesProgress(Long courseId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long studentId = userPrincipal.getId();

        List<CourseNote> notes = courseNoteRepository.findByCourseIdOrderByOrderAsc(courseId);
        long totalNotes = notes.size();

        List<CourseNoteResponse> noteResponses = notes.stream().map(note -> {
            boolean isRead = noteProgressRepository.existsByStudentIdAndCourseNoteId(studentId, note.getId());
            return new CourseNoteResponse(note.getId(), note.getTitle(), note.getContent(), note.getOrder(), isRead);
        }).collect(Collectors.toList());

        long completedNotes = noteResponses.stream().filter(CourseNoteResponse::isRead).count();

        boolean quizUnlocked = totalNotes > 0 && completedNotes == totalNotes;

        return new NotesProgressResponse(noteResponses, completedNotes, totalNotes, quizUnlocked);
    }
}