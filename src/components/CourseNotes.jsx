import React, { useState, useEffect } from 'react';
import { useData } from '../context/DataContext';
import { FileText, CheckCircle, Clock, ArrowRight } from 'lucide-react';

function CourseNotes({ courseId, onNotesCompleted }) {
  const { fetchCourseNotes, markNoteAsRead } = useData();
  
  const [notes, setNotes] = useState([]);
  const [completedCount, setCompletedCount] = useState(0);
  const [totalCount, setTotalCount] = useState(0);
  const [quizUnlocked, setQuizUnlocked] = useState(false);
  
  const [currentNoteIndex, setCurrentNoteIndex] = useState(0);
  const [showNote, setShowNote] = useState(false);

  const loadNotes = async () => {
    const { notes: fetchedNotes, completedCount, totalCount, quizUnlocked } = await fetchCourseNotes(courseId);
    setNotes(fetchedNotes);
    setCompletedCount(completedCount);
    setTotalCount(totalCount);
    setQuizUnlocked(quizUnlocked);
    if (onNotesCompleted) {
      onNotesCompleted(quizUnlocked);
    }
  };

  useEffect(() => {
    loadNotes();
  }, [courseId]);

  const handleReadNote = async (noteId) => {
    try {
      await markNoteAsRead(courseId, noteId);
      await loadNotes();
    } catch (error) {
      console.error("Failed to mark note as read:", error);
    }
  };
  
  const openNote = (index) => {
    setCurrentNoteIndex(index);
    setShowNote(true);
  };

  const closeNote = () => {
    setShowNote(false);
  };

  const nextNote = () => {
    if (currentNoteIndex < notes.length - 1) {
      setCurrentNoteIndex(currentNoteIndex + 1);
    }
  };

  const prevNote = () => {
    if (currentNoteIndex > 0) {
      setCurrentNoteIndex(currentNoteIndex - 1);
    }
  };

  if (totalCount === 0) {
    return (
      <div className="text-center py-8">
        <FileText className="mx-auto h-12 w-12 text-gray-400 mb-4" />
        <p className="text-gray-500">No course notes available yet.</p>
      </div>
    );
  }

  const progressPercentage = totalCount > 0 ? (completedCount / totalCount) * 100 : 0;

  return (
    <div className="space-y-6">
      {/* Progress Overview */}
      <div className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl p-6">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-lg font-semibold text-gray-900">Course Notes Progress</h3>
          <div className="flex items-center space-x-2">
            {quizUnlocked ? (
              <CheckCircle className="w-6 h-6 text-green-600" />
            ) : (
              <Clock className="w-6 h-6 text-yellow-600" />
            )}
            <span className="text-sm font-medium text-gray-700">
              {completedCount}/{totalCount} completed
            </span>
          </div>
        </div>
        
        <div className="w-full bg-gray-200 rounded-full h-3 mb-4">
          <div 
            className="bg-gradient-to-r from-blue-500 to-indigo-600 h-3 rounded-full transition-all duration-300"
            style={{ width: `${progressPercentage}%` }}
          ></div>
        </div>
        
        {quizUnlocked ? (
          <div className="bg-green-100 border border-green-200 rounded-lg p-3">
            <p className="text-green-800 font-medium">
              🎉 Congratulations! You've completed all course notes. You can now take the quiz.
            </p>
          </div>
        ) : (
          <p className="text-gray-600">
            Complete all notes to unlock the quiz and test your knowledge.
          </p>
        )}
      </div>

      {/* Notes List */}
      <div className="grid gap-4">
        {notes.map((note, index) => (
          <div
            key={note.id}
            className={`border rounded-xl p-4 transition-all duration-200 cursor-pointer hover:shadow-md ${
              note.read 
                ? 'bg-green-50 border-green-200' 
                : 'bg-white border-gray-200 hover:border-blue-300'
            }`}
            onClick={() => openNote(index)}
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <div className={`w-8 h-8 rounded-full flex items-center justify-center ${
                  note.read ? 'bg-green-500' : 'bg-gray-300'
                }`}>
                  {note.read ? (
                    <CheckCircle className="w-5 h-5 text-white" />
                  ) : (
                    <span className="text-white font-semibold text-sm">{index + 1}</span>
                  )}
                </div>
                <div>
                  <h4 className="font-semibold text-gray-900">{note.title}</h4>
                  <p className="text-sm text-gray-600">
                    {note.read ? 'Completed' : 'Click to read'}
                  </p>
                </div>
              </div>
              <ArrowRight className="w-5 h-5 text-gray-400" />
            </div>
          </div>
        ))}
      </div>

      {/* Note Reader Modal */}
      {showNote && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
            <div className="sticky top-0 bg-white border-b px-6 py-4">
              <div className="flex justify-between items-center">
                <div>
                  <h2 className="text-xl font-bold text-gray-900">
                    {notes[currentNoteIndex]?.title}
                  </h2>
                  <p className="text-sm text-gray-600">
                    Note {currentNoteIndex + 1} of {notes.length}
                  </p>
                </div>
                <button
                  onClick={closeNote}
                  className="text-gray-500 hover:text-gray-700 text-2xl"
                >
                  ×
                </button>
              </div>
            </div>

            <div className="p-6">
              <div className="prose max-w-none">
                <div className="whitespace-pre-wrap text-gray-700 leading-relaxed">
                  {notes[currentNoteIndex]?.content}
                </div>
              </div>
            </div>

            <div className="sticky bottom-0 bg-white border-t px-6 py-4">
              <div className="flex justify-between items-center">
                <button
                  onClick={prevNote}
                  disabled={currentNoteIndex === 0}
                  className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Previous
                </button>

                <div className="flex space-x-3">
                  {!notes[currentNoteIndex]?.read && (
                    <button
                      onClick={() => handleReadNote(notes[currentNoteIndex]?.id)}
                      className="bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 transition-colors"
                    >
                      Mark as Read
                    </button>
                  )}
                  
                  <button
                    onClick={closeNote}
                    className="bg-gray-600 text-white px-6 py-2 rounded-lg hover:bg-gray-700 transition-colors"
                  >
                    Close
                  </button>
                </div>

                <button
                  onClick={nextNote}
                  disabled={currentNoteIndex === notes.length - 1}
                  className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Next
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default CourseNotes;