package com.lms.dto;

import java.util.List;

public class NotesProgressResponse {
    private List<CourseNoteResponse> notes;
    private long completedCount;
    private long totalCount;
    private boolean quizUnlocked;

    public NotesProgressResponse(List<CourseNoteResponse> notes, long completedCount, long totalCount, boolean quizUnlocked) {
        this.notes = notes;
        this.completedCount = completedCount;
        this.totalCount = totalCount;
        this.quizUnlocked = quizUnlocked;
    }

    public List<CourseNoteResponse> getNotes() {
        return notes;
    }

    public void setNotes(List<CourseNoteResponse> notes) {
        this.notes = notes;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isQuizUnlocked() {
        return quizUnlocked;
    }

    public void setQuizUnlocked(boolean quizUnlocked) {
        this.quizUnlocked = quizUnlocked;
    }
} 