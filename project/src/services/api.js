import axios from 'axios';

const API_BASE_URL = 'https://lms-sbo4.onrender.com/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Handle token expiration
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
};

// Course API
export const courseAPI = {
  getAllCourses: () => api.get('/courses'),
  getCourse: (id) => api.get(`/courses/${id}`),
  createCourse: (courseData) => api.post('/courses', courseData),
  updateCourse: (id, courseData) => api.put(`/courses/${id}`, courseData),
  deleteCourse: (id) => api.delete(`/courses/${id}`),
  getTeacherCourses: () => api.get('/courses/teacher'),
};

// Enrollment API
export const enrollmentAPI = {
  enrollInCourse: (courseId) => api.post(`/enrollments/${courseId}`),
  getStudentEnrollments: () => api.get('/enrollments/student'),
  getCourseEnrollments: (courseId) => api.get(`/enrollments/course/${courseId}`),
  getNotesProgress: (courseId) => api.get(`/enrollments/${courseId}/notes-progress`),
};

// Quiz API
export const quizAPI = {
  getQuiz: (courseId) => api.get(`/quizzes/course/${courseId}`),
  submitQuiz: (courseId, answers) => api.post(`/quizzes/course/${courseId}/submit`, answers),
};

// Course Notes API
export const courseNotesAPI = {
  getCourseNotes: (courseId) => api.get(`/courses/${courseId}/notes`),
  createCourseNote: (courseId, noteData) => api.post(`/courses/${courseId}/notes`, noteData),
  updateCourseNote: (courseId, noteId, noteData) => api.put(`/courses/${courseId}/notes/${noteId}`, noteData),
  deleteCourseNote: (courseId, noteId) => api.delete(`/courses/${courseId}/notes/${noteId}`),
  markNoteAsRead: (courseId, noteId) => api.post(`/courses/${courseId}/notes/${noteId}/mark-read`),
};

export default api;
