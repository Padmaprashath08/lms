import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useData } from '../context/DataContext';
import { BookOpen, Award, Clock, Play } from 'lucide-react';

function StudentDashboard() {
  const { user } = useAuth();
  const { getEnrollmentsByStudent, courses, enrollInCourse, getAvailableCourses } = useData();

  const enrollments = getEnrollmentsByStudent();
  const availableCourses = getAvailableCourses();

  const handleEnroll = async (courseId) => {
    try {
      await enrollInCourse(courseId);
    } catch (error) {
      alert('Error enrolling in course');
    }
  };

  const getEnrolledCourses = () => {
    return enrollments.map(enrollment => {
      const course = courses.find(c => c.id === enrollment.courseId);
      return { ...course, enrollment };
    }).filter(course => course.id); // Filter out courses that might not exist
  };

  const enrolledCourses = getEnrolledCourses();
  const completedCourses = enrolledCourses.filter(c => c.enrollment.completed);
  const averageScore = completedCourses.length > 0
    ? Math.round(completedCourses.reduce((sum, c) => sum + (c.enrollment.score || 0), 0) / completedCourses.length)
    : 0;

  return (
    <div className="max-w-7xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Student Dashboard</h1>
        <p className="text-gray-600">Welcome back, {user.name}!</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-white rounded-xl shadow-lg p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Enrolled Courses</p>
              <p className="text-2xl font-bold text-gray-900">{enrolledCourses.length}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-lg p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Completed</p>
              <p className="text-2xl font-bold text-gray-900">{completedCourses.length}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-lg p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Average Score</p>
              <p className="text-2xl font-bold text-gray-900">{averageScore}%</p>
            </div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="bg-white rounded-xl shadow-lg p-6">
          <h2 className="text-xl font-semibold text-gray-900 mb-6">My Courses</h2>
          
          {enrolledCourses.length === 0 ? (
            <div className="text-center py-8">
              <p className="text-gray-500">No courses enrolled yet.</p>
            </div>
          ) : (
            <div className="space-y-4">
              {enrolledCourses.map((course) => (
                <div key={course.id} className="border border-gray-200 rounded-lg p-4">
                  <div className="flex justify-between items-start mb-2">
                    <h3 className="font-semibold text-gray-900">{course.title}</h3>
                    <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                      course.enrollment.completed 
                        ? 'bg-green-100 text-green-800' 
                        : 'bg-yellow-100 text-yellow-800'
                    }`}>
                      {course.enrollment.completed ? 'Completed' : 'In Progress'}
                    </span>
                  </div>
                  
                  <p className="text-gray-600 text-sm mb-3">{course.description}</p>
                  <p className="text-sm text-gray-500 mb-4">Teacher: {course.teacherName}</p>
                  
                  {course.enrollment.completed ? (
                    <div className="flex items-center justify-between">
                      <span className="text-green-600 font-medium">
                        Score: {course.enrollment.score}%
                      </span>
                      <Link
                        to={`/course/${course.id}`}
                        className="text-blue-600 hover:text-blue-800 text-sm"
                      >
                        View Details
                      </Link>
                    </div>
                  ) : (
                    <div className="flex space-x-2">
                      <Link
                        to={`/course/${course.id}`}
                        className="flex-1 bg-blue-100 text-blue-700 px-3 py-2 rounded text-sm text-center hover:bg-blue-200 transition-colors"
                      >
                        Continue Learning
                      </Link>
                      <Link
                        to={`/quiz/${course.id}`}
                        className="bg-green-100 text-green-700 px-3 py-2 rounded text-sm hover:bg-green-200 transition-colors flex items-center space-x-1"
                      >
                        <Play size={16} />
                        <span>Take Quiz</span>
                      </Link>
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="bg-white rounded-xl shadow-lg p-6">
          <h2 className="text-xl font-semibold text-gray-900 mb-6">Available Courses</h2>
          
          {availableCourses.length === 0 ? (
            <div className="text-center py-8">
              <BookOpen className="mx-auto h-12 w-12 text-gray-400 mb-4" />
              <p className="text-gray-500">No new courses available.</p>
            </div>
          ) : (
            <div className="space-y-4">
              {availableCourses.map((course) => (
                <div key={course.id} className="border border-gray-200 rounded-lg p-4">
                  <h3 className="font-semibold text-gray-900 mb-2">{course.title}</h3>
                  <p className="text-gray-600 text-sm mb-3">{course.description}</p>
                  <p className="text-sm text-gray-500 mb-4">Teacher: {course.teacherName}</p>
                  
                  <button
                    onClick={() => handleEnroll(course.id)}
                    className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition-colors"
                  >
                    Enroll Now
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default StudentDashboard;