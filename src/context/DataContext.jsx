import React, { createContext, useContext, useState, useEffect } from 'react';
import { courseAPI, enrollmentAPI, quizAPI, courseNotesAPI } from '../services/api';
import { useAuth } from './AuthContext';

const DataContext = createContext();

export function DataProvider({ children }) {
  const [courses, setCourses] = useState([]);
  const [enrollments, setEnrollments] = useState([]);
  const { user } = useAuth();

  useEffect(() => {
    if (user) {
      loadData();
    }
  }, [user]);

  const loadData = async () => {
    try {
      const [coursesRes, enrollmentsRes] = await Promise.all([
        courseAPI.getAllCourses(),
        user.role === 'STUDENT' ? enrollmentAPI.getStudentEnrollments() : Promise.resolve({ data: [] })
      ]);
      
      setCourses(coursesRes.data);
      setEnrollments(enrollmentsRes.data);
    } catch (error) {
      console.error('Error loading data:', error);
    }
  };

  const createCourse = async (courseData) => {
    try {
      const response = await courseAPI.createCourse(courseData);
      setCourses([...courses, response.data]);
      return response.data;
    } catch (error) {
      console.error('Error creating course:', error);
      throw error;
    }
  };

  const updateCourse = async (id, courseData) => {
    try {
      const response = await courseAPI.updateCourse(id, courseData);
      setCourses(courses.map(course => 
        course.id === id ? response.data : course
      ));
      return response.data;
    } catch (error) {
      console.error('Error updating course:', error);
      throw error;
    }
  };

  const deleteCourse = async (id) => {
    try {
      await courseAPI.deleteCourse(id);
      // After deleting, reload the main data to ensure consistency
      await loadData();
    } catch (error) {
      console.error('Error deleting course:', error);
      throw error;
    }
  };

  const addCourseNote = async (courseId, noteData) => {
    try {
      const response = await courseNotesAPI.createCourseNote(courseId, noteData);
      // Optionally, refresh notes from backend here if you keep local state
      return response.data;
    } catch (error) {
      console.error('Error adding course note:', error);
      throw error;
    }
  };

  const updateCourseNote = async (courseId, noteId, noteData) => {
    try {
      const response = await courseNotesAPI.updateCourseNote(courseId, noteId, noteData);
      // Optionally, refresh notes from backend here if you keep local state
      return response.data;
    } catch (error) {
      console.error('Error updating course note:', error);
      throw error;
    }
  };

  const deleteCourseNote = async (courseId, noteId) => {
    try {
      await courseNotesAPI.deleteCourseNote(courseId, noteId);
      // Optionally, refresh notes from backend here if you keep local state
    } catch (error) {
      console.error('Error deleting course note:', error);
      throw error;
    }
  };

  const markNoteAsRead = async (courseId, noteId) => {
    try {
      await courseNotesAPI.markNoteAsRead(courseId, noteId);
    } catch (error) {
      console.error('Error marking note as read:', error);
      throw error;
    }
  };

  const enrollInCourse = async (courseId) => {
    try {
      const response = await enrollmentAPI.enrollInCourse(courseId);
      setEnrollments([...enrollments, response.data]);
      return response.data;
    } catch (error) {
      console.error('Error enrolling in course:', error);
      throw error;
    }
  };

  const submitQuiz = async (courseId, answers) => {
    try {
      const response = await quizAPI.submitQuiz(courseId, { answers });
      
      // Update enrollment with completion status
      setEnrollments(enrollments.map(enrollment => 
        enrollment.courseId === courseId
          ? { ...enrollment, completed: true, score: response.data.score }
          : enrollment
      ));
      
      return response.data.score;
    } catch (error) {
      console.error('Error submitting quiz:', error);
      throw error;
    }
  };

  const getCoursesByTeacher = () => {
    return courses.filter(course => course.teacherId === user?.id);
  };

  const getEnrollmentsByStudent = () => {
    return enrollments;
  };

  const getAvailableCourses = () => {
    const enrolledCourseIds = enrollments.map(e => e.courseId);
    return courses.filter(course => !enrolledCourseIds.includes(course.id));
  };

  const getEnrolledStudents = async (courseId) => {
    try {
      const response = await enrollmentAPI.getCourseEnrollments(courseId);
      return response.data;
    } catch (error) {
      console.error('Error getting enrolled students:', error);
      return [];
    }
  };

  const fetchCourseNotes = async (courseId) => {
    try {
      const response = await enrollmentAPI.getNotesProgress(courseId);
      return response.data;
    } catch (error) {
      console.error('Error fetching course notes:', error);
      return {
        notes: [],
        completedCount: 0,
        totalCount: 0,
        quizUnlocked: false,
      };
    }
  };

  const fetchNotesForTeacher = async (courseId) => {
    try {
      const response = await courseNotesAPI.getCourseNotes(courseId);
      return response.data;
    } catch (error) {
      console.error('Error fetching course notes for teacher:', error);
      return [];
    }
  };

  return (
    <DataContext.Provider value={{
      courses,
      enrollments,
      createCourse,
      updateCourse,
      deleteCourse,
      enrollInCourse,
      submitQuiz,
      addCourseNote,
      updateCourseNote,
      deleteCourseNote,
      markNoteAsRead,
      getCoursesByTeacher,
      getEnrollmentsByStudent,
      getAvailableCourses,
      getEnrolledStudents,
      loadData,
      fetchCourseNotes,
      fetchNotesForTeacher
    }}>
      {children}
    </DataContext.Provider>
  );
}

export function useData() {
  const context = useContext(DataContext);
  if (context === undefined) {
    throw new Error('useData must be used within a DataProvider');
  }
  return context;
}