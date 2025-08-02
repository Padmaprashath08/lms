import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useData } from '../context/DataContext';
import { useAuth } from '../context/AuthContext';
import { BookOpen, User, Clock, Play, Award, FileText } from 'lucide-react';
import CourseNotes from '../components/CourseNotes';
import { quizAPI } from '../services/api';

function CourseDetails() {
  const { id } = useParams();
  const { user } = useAuth();
  const { courses, getEnrollmentsByStudent } = useData();
  const [quiz, setQuiz] = useState(null);
  const [quizLoading, setQuizLoading] = useState(true);
  const [canTakeQuiz, setCanTakeQuiz] = useState(false);

  const course = courses.find(c => c.id === parseInt(id));
  const enrollments = getEnrollmentsByStudent();
  const enrollment = enrollments.find(e => e.courseId === parseInt(id));

  useEffect(() => {
    const fetchQuiz = async () => {
      setQuizLoading(true);
      try {
        const response = await quizAPI.getQuiz(id);
        setQuiz(response.data);
      } catch (error) {
        setQuiz(null);
      } finally {
        setQuizLoading(false);
      }
    };
    fetchQuiz();
  }, [id]);

  const handleNotesCompleted = (completed) => {
    setCanTakeQuiz(completed);
  };

  if (!course) {
    return (
      <div className="max-w-4xl mx-auto text-center py-12">
        <BookOpen className="mx-auto h-16 w-16 text-gray-400 mb-4" />
        <h1 className="text-2xl font-bold text-gray-900 mb-2">Course Not Found</h1>
        <p className="text-gray-600">The course you're looking for doesn't exist.</p>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
        <div className="bg-gradient-to-r from-blue-600 to-purple-600 px-8 py-12 text-white">
          <h1 className="text-4xl font-bold mb-4">{course.title}</h1>
          <p className="text-xl opacity-90 mb-6">{course.description}</p>
          <div className="flex items-center space-x-6 text-sm">
            <div className="flex items-center space-x-2">
              
              <span>Instructor: {course.teacherName}</span>
            </div>
            {enrollment && (
              <div className="flex items-center space-x-2">
                
                <span>Enrolled: {new Date(enrollment.enrolledAt).toLocaleDateString()}</span>
              </div>
            )}
          </div>
        </div>
        <div className="p-8">
          {enrollment ? (
            <div className="space-y-6">
              {/* Course Notes Section */}
              <div className="bg-blue-50 rounded-xl p-6">
                <div className="flex items-center space-x-3 mb-4">
                
                  <h3 className="text-lg font-semibold text-gray-900">Course Materials</h3>
                </div>
                <p className="text-gray-600 mb-4">
                  Study the course notes below to understand the material before taking the quiz.
                </p>
                <CourseNotes courseId={parseInt(id)} onNotesCompleted={handleNotesCompleted} />
              </div>

              {/* Unified Quiz Section */}
              <div className="bg-blue-50 rounded-xl p-6 flex flex-col md:flex-row md:items-center md:justify-between border border-blue-100">
                <div>
                  <div className="flex items-center mb-2">
                    <span className="font-semibold text-lg text-gray-900">Course Assessment</span>
                  </div>
                  <div className="text-gray-600 mb-2">
                    {enrollment.completed
                      ? 'Course completed!'
                      : canTakeQuiz
                        ? 'Ready to take the quiz'
                        : 'Complete all notes to unlock the quiz'}
                  </div>
                  {!quizLoading && quiz && quiz.questions && quiz.questions.length > 0 && (
                    <div className="bg-yellow-50 rounded-lg p-4 mt-2">
                      <div className="font-semibold text-lg mb-1 text-gray-900">Quiz Information</div>
                      <div className="text-gray-700 mb-3">
                        This course includes a quiz with {quiz.questions.length} questions. Complete all course notes first, then take the quiz to finish the course.
                      </div>
                    </div>
                  )}
                </div>
                <div className="mt-4 md:mt-0">
                  {enrollment.completed ? (
                    <div className="flex items-center space-x-2 text-green-600">
                      <span className="font-semibold">Score: {enrollment.score}%</span>
                    </div>
                  ) : canTakeQuiz && quiz && quiz.questions && quiz.questions.length > 0 ? (
                    <Link
                      to={`/quiz/${course.id}`}
                      className="bg-blue-600 hover:bg-blue-700 text-white font-semibold px-6 py-3 rounded-lg flex items-center"
                    >
                    Take Quiz
                    </Link>
                  ) : (
                    <div className="bg-gray-100 text-gray-500 px-6 py-3 rounded-lg flex items-center space-x-2">
                      <span>Quiz Locked</span>
                    </div>
                  )}
                </div>
              </div>
            </div>
          ) : (
            <div className="text-center py-12">
              <h2 className="text-2xl font-bold text-gray-900 mb-2">Course Not Enrolled</h2>
              <p className="text-gray-600 mb-6">You need to enroll in this course to view its content.</p>
              <Link
                to={user.role === 'STUDENT' ? '/student' : '/teacher'}
                className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors"
              >
                Back to Dashboard
              </Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default CourseDetails;