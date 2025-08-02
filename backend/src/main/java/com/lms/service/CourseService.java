package com.lms.service;

import com.lms.dto.CourseRequest;
import com.lms.dto.CourseResponse;
import com.lms.entity.Course;
import com.lms.entity.Question;
import com.lms.entity.Quiz;
import com.lms.entity.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.QuestionRepository;
import com.lms.repository.QuizRepository;
import com.lms.repository.UserRepository;
import com.lms.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToCourseResponse)
                .collect(Collectors.toList());
    }

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return convertToCourseResponse(course);
    }

    public List<CourseResponse> getTeacherCourses() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User teacher = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        return courseRepository.findByTeacher(teacher).stream()
                .map(this::convertToCourseResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public String createCourse(CourseRequest courseRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User teacher = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Course course = new Course();
        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        course.setTeacher(teacher);

        Course savedCourse = courseRepository.save(course);

        // Create quiz if provided
        if (courseRequest.getQuiz() != null && courseRequest.getQuiz().getQuestions() != null) {
            Quiz quiz = new Quiz();
            quiz.setTitle(courseRequest.getQuiz().getTitle());
            quiz.setCourse(savedCourse);
            Quiz savedQuiz = quizRepository.save(quiz);

            // Create questions
            for (CourseRequest.QuestionRequest questionRequest : courseRequest.getQuiz().getQuestions()) {
                if (questionRequest.getQuestion() != null && !questionRequest.getQuestion().trim().isEmpty()) {
                    Question question = new Question();
                    question.setQuestion(questionRequest.getQuestion());
                    question.setOptions(questionRequest.getOptions());
                    question.setCorrectAnswer(questionRequest.getCorrectAnswer());
                    question.setQuiz(savedQuiz);
                    questionRepository.save(question);
                }
            }
        }

//        return convertToCourseResponse(savedCourse);
        return "Courses added successfully";
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest courseRequest) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!course.getTeacher().getId().equals(userPrincipal.getId())) {
            throw new RuntimeException("Not authorized to update this course");
        }

        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());

        // Update quiz
        Quiz existingQuiz = quizRepository.findByCourseId(id).orElse(null);
        if (existingQuiz != null) {
            // Delete existing questions
            questionRepository.deleteAll(existingQuiz.getQuestions());
            
            // Update quiz title
            existingQuiz.setTitle(courseRequest.getQuiz().getTitle());
            quizRepository.save(existingQuiz);

            // Create new questions
            if (courseRequest.getQuiz().getQuestions() != null) {
                for (CourseRequest.QuestionRequest questionRequest : courseRequest.getQuiz().getQuestions()) {
                    if (questionRequest.getQuestion() != null && !questionRequest.getQuestion().trim().isEmpty()) {
                        Question question = new Question();
                        question.setQuestion(questionRequest.getQuestion());
                        question.setOptions(questionRequest.getOptions());
                        question.setCorrectAnswer(questionRequest.getCorrectAnswer());
                        question.setQuiz(existingQuiz);
                        questionRepository.save(question);
                    }
                }
            }
        }

        Course savedCourse = courseRepository.save(course);
        return convertToCourseResponse(savedCourse);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!course.getTeacher().getId().equals(userPrincipal.getId())) {
            throw new RuntimeException("Not authorized to delete this course");
        }

        courseRepository.delete(course);
    }

    private CourseResponse convertToCourseResponse(Course course) {
        CourseResponse response = new CourseResponse(course);
        
        // Add quiz information if exists
        Quiz quiz = quizRepository.findByCourseId(course.getId()).orElse(null);
        if (quiz != null) {
            response.setQuiz(new com.lms.dto.QuizResponse(quiz));
        }
        
        return response;
    }
}