import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useData } from '../context/DataContext';
import { Plus, Users, BookOpen, Award, FileText } from 'lucide-react';
import CourseForm from '../components/CourseForm';
import NotesManager from '../components/NotesManager';

function TeacherDashboard() {
  const { user } = useAuth();
  const { getCoursesByTeacher, getEnrolledStudents } = useData();
  const [showCourseForm, setShowCourseForm] = useState(false);
  const [showNotesManager, setShowNotesManager] = useState(false);
  const [selectedCourseForNotes, setSelectedCourseForNotes] = useState(null);
  const [selectedCourse, setSelectedCourse] = useState(null);
  const [enrolledStudents, setEnrolledStudents] = useState([]);

  
  

  const courses = getCoursesByTeacher();

  const handleManageNotes = (course) => {
    setSelectedCourseForNotes(course);
    setShowNotesManager(true);
  };
  
  const handleViewStudents = async (course) => {
    setSelectedCourse(course);
    try {
      const students = await getEnrolledStudents(course.id);
      setEnrolledStudents(students);
    } catch (error) {
      console.error('Error fetching students:', error);
      setEnrolledStudents([]);
    }
  };

  const totalStudents = courses.reduce((total, course) => {
    return total + (course.enrolledCount || 0);
  }, 0);



  return (
    <div className="max-w-7xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Teacher Dashboard</h1>
        <p className="text-gray-600">Welcome back, {user.name}!</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-white rounded-xl shadow-lg p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Total Courses</p>
              <p className="text-2xl font-bold text-gray-900">{courses.length}</p>
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-xl shadow-lg p-6 mb-8">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold text-gray-900">My Courses</h2>
          <button
            onClick={() => {
              setShowCourseForm(true);
            }}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg flex items-center space-x-2 hover:bg-blue-700 transition-colors"
          >
            <span>Create Course</span>
          </button>
        </div>

        {courses.length === 0 ? (
          <div className="text-center py-12">
            <BookOpen className="mx-auto h-12 w-12 text-gray-400 mb-4" />
            <p className="text-gray-500">No courses created yet. Create your first course!</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {courses.map((course) => (
              <div key={course.id} className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
                <h3 className="font-semibold text-gray-900 mb-2">{course.title}</h3>
                <p className="text-gray-600 text-sm mb-4 line-clamp-2">{course.description}</p>

                <div className="grid grid-cols-2 gap-2">
                  <button
                    onClick={() => handleViewStudents(course)}
                    className="bg-green-100 text-green-700 px-3 py-2 rounded text-sm hover:bg-green-200 transition-colors"
                  >
                    View Students
                  </button>
                  <button
                    onClick={() => handleManageNotes(course)}
                    className="bg-purple-100 text-purple-700 px-3 py-2 rounded text-sm hover:bg-purple-200 transition-colors flex items-center justify-center space-x-1"
                  >
                    <span>Notes</span>
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {selectedCourse && (
        <div className="bg-white rounded-xl shadow-lg p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-semibold text-gray-900">
              Students in "{selectedCourse.title}"
            </h2>
            <button
              onClick={() => setSelectedCourse(null)}
              className="text-gray-500 hover:text-gray-700"
            >
              ×
            </button>
          </div>

          {enrolledStudents.length === 0 ? (
            <div className="text-center py-8">
              <Users className="mx-auto h-12 w-12 text-gray-400 mb-4" />
              <p className="text-gray-500">No students enrolled yet.</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Student Name
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Email
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Enrolled Date
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Status
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Score
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {enrolledStudents.map((student) => (
                    <tr key={student.id}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {student.studentName}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {student.studentEmail}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {new Date(student.enrolledAt).toLocaleDateString()}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                          student.completed 
                            ? 'bg-green-100 text-green-800' 
                            : 'bg-yellow-100 text-yellow-800'
                        }`}>
                          {student.completed ? 'Completed' : 'In Progress'}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {student.score ? `${student.score}%` : '-'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {showNotesManager && selectedCourseForNotes && (
        <NotesManager
          courseId={selectedCourseForNotes.id}
          courseTitle={selectedCourseForNotes.title}
          onClose={() => {
            setShowNotesManager(false);
            setSelectedCourseForNotes(null);
          }}
        />
      )}
      {showCourseForm && (
        <CourseForm
          onClose={() => {
            setShowCourseForm(false);
          }}
        />
      )}
    </div>
  );
}

export default TeacherDashboard;