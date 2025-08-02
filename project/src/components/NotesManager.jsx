import React, { useState, useEffect } from 'react';
import { useData } from '../context/DataContext';
import { Plus, Edit, Trash2, FileText, Save, X } from 'lucide-react';

function NotesManager({ courseId, courseTitle, onClose }) {
  const { fetchNotesForTeacher, addCourseNote, updateCourseNote, deleteCourseNote } = useData();
  const [notes, setNotes] = useState([]);
  const [editingNote, setEditingNote] = useState(null);
  const [newNote, setNewNote] = useState({ title: '', content: '' });
  const [showAddForm, setShowAddForm] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);

  const loadNotes = async () => {
    const latestNotes = await fetchNotesForTeacher(courseId);
    setNotes(latestNotes);
  };

  useEffect(() => {
    loadNotes();
  }, [courseId]);

  const handleAddNote = async () => {
    if (newNote.title.trim()) {
      await addCourseNote(courseId, newNote);
      await loadNotes();
      setNewNote({ title: '', content: '' });
      setShowAddForm(false);
    }
  };

  const handleUpdateNote = async (noteId, updatedData) => {
    await updateCourseNote(courseId, noteId, updatedData);
    await loadNotes();
    setEditingNote(null);
  };

  const handleDeleteNote = async (noteId) => {
    if (window.confirm('Are you sure you want to delete this note?')) {
      await deleteCourseNote(courseId, noteId);
      await loadNotes();
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
        <div className="flex justify-between items-center p-6 border-b">
          <h2 className="text-2xl font-bold text-gray-900">
            Notes for "{courseTitle}"
          </h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700"
          >
            <X size={24} />
          </button>
        </div>

        <div className="p-6">
          <div className="flex justify-between items-center mb-6">
            <h3 className="text-lg font-semibold text-gray-900">
              Manage Course Notes ({notes.length})
            </h3>
            <button
              onClick={() => setShowAddForm(true)}
              className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center space-x-2"
            >
              <span>Add Note</span>
            </button>
          </div>

          {showAddForm && (
            <div className="bg-gray-50 rounded-lg p-4 mb-6">
              <h4 className="font-medium text-gray-900 mb-3">Add New Note</h4>
              <div className="space-y-3">
                <input
                  type="text"
                  value={newNote.title}
                  onChange={(e) => setNewNote({ ...newNote, title: e.target.value })}
                  placeholder="Note title"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
                <textarea
                  value={newNote.content}
                  onChange={(e) => setNewNote({ ...newNote, content: e.target.value })}
                  placeholder="Note content"
                  rows={4}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                />
                <div className="flex space-x-2">
                  <button
                    onClick={handleAddNote}
                    className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors flex items-center space-x-2"
                  >
                    <span>Save Note</span>
                  </button>
                  <button
                    onClick={() => {
                      setShowAddForm(false);
                      setNewNote({ title: '', content: '' });
                    }}
                    className="bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors"
                  >
                    Cancel
                  </button>
                </div>
              </div>
            </div>
          )}

          {notes.length === 0 ? (
            <div className="text-center py-12">
              <FileText className="mx-auto h-12 w-12 text-gray-400 mb-4" />
              <p className="text-gray-500">No notes added yet. Create your first note!</p>
            </div>
          ) : (
            <div className="space-y-4">
              {notes.map((note, index) => (
                <div key={note.id} className="bg-gray-50 rounded-lg p-4">
                  {editingNote === note.id ? (
                    <EditNoteForm
                      note={note}
                      onSave={(updatedData) => handleUpdateNote(note.id, updatedData)}
                      onCancel={() => setEditingNote(null)}
                    />
                  ) : (
                    <div>
                      <div className="flex justify-between items-start mb-2">
                        <div className="flex items-center space-x-2">
                          <span className="bg-blue-100 text-blue-800 text-xs font-medium px-2 py-1 rounded-full">
                            Note {index + 1}
                          </span>
                          <h4 className="font-semibold text-gray-900">{note.title}</h4>
                        </div>
                        <div className="flex space-x-2">
                          <button
                            onClick={() => { setEditingNote(note.id); }}
                            className="text-blue-600 hover:text-blue-700"
                          >
                            Edit
                          </button>
                          <button
                            onClick={() => handleDeleteNote(note.id)}
                            className="text-red-600 hover:text-red-700"
                          >
                            Delete
                          </button>
                        </div>
                      </div>
                      <div className="text-gray-700 whitespace-pre-wrap">{note.content}</div>
                      <div className="text-xs text-gray-500 mt-2">
                        Created: {new Date(note.createdAt).toLocaleDateString()}
                      </div>
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

function EditNoteForm({ note, onSave, onCancel }) {
  const [title, setTitle] = useState(note.title);
  const [content, setContent] = useState(note.content);

  const handleSave = () => {
    if (title.trim() && content.trim()) {
      onSave({ title, content });
    }
  };

  return (
    <div className="space-y-3">
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
      />
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        rows={4}
        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
      />
      <div className="flex space-x-2">
        <button
          onClick={handleSave}
          className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors flex items-center space-x-2"
        >
          <span>Save</span>
        </button>
        <button
          onClick={onCancel}
          className="bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors"
        >
          Cancel
        </button>
      </div>
    </div>
  );
}

export default NotesManager;