package com.lms.service;

import com.lms.dto.CourseNoteRequest;
import com.lms.dto.CourseNoteResponse;
import com.lms.entity.Course;
import com.lms.entity.CourseNote;
import com.lms.entity.NoteProgress;
import com.lms.entity.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.CourseNoteRepository;
import com.lms.repository.NoteProgressRepository;
import com.lms.repository.UserRepository;
import com.lms.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseNoteService {

    @Autowired
    private CourseNoteRepository courseNoteRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteProgressRepository noteProgressRepository;

    public List<CourseNoteResponse> getCourseNotes(Long courseId, boolean isStudent) {
        List<CourseNote> notes = courseNoteRepository.findByCourseIdOrderByOrderAsc(courseId);

        if (isStudent) {
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return notes.stream().map(note -> {
                boolean isRead = noteProgressRepository.existsByStudentIdAndCourseNoteId(userPrincipal.getId(), note.getId());
                return new CourseNoteResponse(note.getId(), note.getTitle(), note.getContent(), note.getOrder(), isRead);
            }).collect(Collectors.toList());
        } else {
            return notes.stream()
                    .map(CourseNoteResponse::new)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public CourseNoteResponse createCourseNote(Long courseId, CourseNoteRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if user is the teacher of this course
        if (!course.getTeacher().getId().equals(userPrincipal.getId())) {
            throw new RuntimeException("Not authorized to add notes to this course");
        }

        // Set order if not provided
        if (request.getOrder() == null) {
            List<CourseNote> existingNotes = courseNoteRepository.findByCourseIdOrderByOrderAsc(courseId);
            request.setOrder(existingNotes.size() + 1);
        }

        CourseNote courseNote = new CourseNote(request.getTitle(), request.getContent(), request.getOrder(), course);
        CourseNote savedNote = courseNoteRepository.save(courseNote);

        return new CourseNoteResponse(savedNote);
    }

    @Transactional
    public CourseNoteResponse updateCourseNote(Long noteId, CourseNoteRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        CourseNote courseNote = courseNoteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Course note not found"));

        // Check if user is the teacher of this course
        if (!courseNote.getCourse().getTeacher().getId().equals(userPrincipal.getId())) {
            throw new RuntimeException("Not authorized to update this note");
        }

        courseNote.setTitle(request.getTitle());
        courseNote.setContent(request.getContent());
        if (request.getOrder() != null) {
            courseNote.setOrder(request.getOrder());
        }

        CourseNote savedNote = courseNoteRepository.save(courseNote);
        return new CourseNoteResponse(savedNote);
    }

    @Transactional
    public void deleteCourseNote(Long noteId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        CourseNote courseNote = courseNoteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Course note not found"));

        // Check if user is the teacher of this course
        if (!courseNote.getCourse().getTeacher().getId().equals(userPrincipal.getId())) {
            throw new RuntimeException("Not authorized to delete this note");
        }

        courseNoteRepository.delete(courseNote);
    }

    @Transactional
    public void markNoteAsRead(Long noteId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User student = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        CourseNote courseNote = courseNoteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Course note not found"));

        // Check if already marked as read
        if (!noteProgressRepository.existsByStudentIdAndCourseNoteId(student.getId(), noteId)) {
            NoteProgress progress = new NoteProgress(student, courseNote);
            noteProgressRepository.save(progress);
        }
    }

    public boolean hasCompletedAllNotes(Long courseId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        long totalNotes = courseNoteRepository.countByCourseId(courseId);
        long completedNotes = noteProgressRepository.countByStudentIdAndCourseNote_CourseId(userPrincipal.getId(), courseId);
        
        return totalNotes > 0 && totalNotes == completedNotes;
    }
}