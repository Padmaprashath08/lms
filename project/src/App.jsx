import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import TeacherDashboard from './pages/TeacherDashboard';
import StudentDashboard from './pages/StudentDashboard';
import CourseDetails from './pages/CourseDetails';
import Quiz from './pages/Quiz';

import { AuthProvider, useAuth } from './context/AuthContext';
import { DataProvider } from './context/DataContext';

function AppContent() {
  const { user } = useAuth();

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <Navbar />
      <main className="container mx-auto px-4 py-8">
        <Routes>
          <Route path="/login" element={!user ? <Login /> : <Navigate to={user.role === 'TEACHER' ? '/teacher' : '/student'} />} />
          <Route path="/register" element={!user ? <Register /> : <Navigate to={user.role === 'TEACHER' ? '/teacher' : '/student'} />} />
          <Route path="/teacher" element={user?.role === 'TEACHER' ? <TeacherDashboard /> : <Navigate to="/login" />} />
          <Route path="/student" element={user?.role === 'STUDENT' ? <StudentDashboard /> : <Navigate to="/login" />} />
          <Route path="/course/:id" element={user ? <CourseDetails /> : <Navigate to="/login" />} />
          <Route path="/quiz/:courseId" element={user?.role === 'STUDENT' ? <Quiz /> : <Navigate to="/login" />} />
          <Route path="/api/auth" element={user ? <Navigate to={user.role === 'TEACHER' ? '/teacher' : '/student'} /> : <Login />} />
        </Routes>
      </main>
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <DataProvider>
        <Router>
          <AppContent />
        </Router>
      </DataProvider>
    </AuthProvider>
  );
}

export default App;
