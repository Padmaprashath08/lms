package com.lms.controller;

import com.lms.dto.CourseNoteRequest;
import com.lms.dto.CourseNoteResponse;
import com.lms.service.CourseNoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/courses/{courseId}/notes")
public class CourseNoteController {

    @Autowired
    private CourseNoteService courseNoteService;

    @GetMapping
    public ResponseEntity<List<CourseNoteResponse>> getCourseNotes(@PathVariable Long courseId, Authentication authentication) {
        boolean isStudent = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"));
        return ResponseEntity.ok(courseNoteService.getCourseNotes(courseId, isStudent));
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseNoteResponse> createCourseNote(
            @PathVariable Long courseId, 
            @Valid @RequestBody CourseNoteRequest request) {
        try {
            return ResponseEntity.ok(courseNoteService.createCourseNote(courseId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{noteId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseNoteResponse> updateCourseNote(
            @PathVariable Long courseId,
            @PathVariable Long noteId, 
            @Valid @RequestBody CourseNoteRequest request) {
        try {
            return ResponseEntity.ok(courseNoteService.updateCourseNote(noteId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{noteId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteCourseNote(@PathVariable Long courseId, @PathVariable Long noteId) {
        try {
            courseNoteService.deleteCourseNote(noteId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{noteId}/mark-read")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> markNoteAsRead(@PathVariable Long courseId, @PathVariable Long noteId) {
        try {
            courseNoteService.markNoteAsRead(noteId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/completion-status")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Boolean>> getNotesCompletionStatus(@PathVariable Long courseId) {
        try {
            boolean completed = courseNoteService.hasCompletedAllNotes(courseId);
            return ResponseEntity.ok(Map.of("allNotesCompleted", completed));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}