package com.lms.controller;

import com.lms.dto.EnrollmentResponse;
import com.lms.dto.NotesProgressResponse;
import com.lms.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> enrollInCourse(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(enrollmentService.enrollInCourse(courseId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentResponse>> getStudentEnrollments() {
        return ResponseEntity.ok(enrollmentService.getStudentEnrollments());
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<EnrollmentResponse>> getCourseEnrollments(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCourseEnrollments(courseId));
    }

    @GetMapping("/{courseId}/notes-progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<NotesProgressResponse> getNotesProgress(@PathVariable Long courseId) {
        NotesProgressResponse response = enrollmentService.getNotesProgress(courseId);
        return ResponseEntity.ok(response);
    }
}