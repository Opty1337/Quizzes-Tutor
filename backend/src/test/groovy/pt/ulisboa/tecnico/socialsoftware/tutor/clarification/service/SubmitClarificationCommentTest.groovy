package pt.ulisboa.tecnico.socialsoftware.tutor.clarification.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.beans.factory.annotation.Autowired
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizQuestionRepository
import spock.lang.Shared
import spock.lang.Unroll
import spock.lang.Specification

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer

import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution

import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException

import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.dto.UserDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository

import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository

import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuizAnswerRepository

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuestionAnswerRepository

import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.domain.ClarificationRequest
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.repository.ClarificationRequestRepository

import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.dto.ClarificationCommentDto
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationCommentService
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.repository.ClarificationCommentRepository

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

import pt.ulisboa.tecnico.socialsoftware.tutor.config.DateHandler;

@DataJpaTest
class SubmitClarificationCommentTest extends Specification {

    public static final String CLARIFICATION_CONTENT = "ClarificationRequest Question"
    public static final String COMMENT_CONTENT = "Teacher Answer"

    @Autowired
    ClarificationCommentService clarificationCommentService

    @Autowired
    UserRepository userRepository

    @Autowired
    CourseRepository courseRepository

    @Autowired
    CourseExecutionRepository courseExecutionRepository

    @Autowired
    QuizRepository quizRepository

    @Autowired
    QuizAnswerRepository quizAnswerRepository

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    QuizQuestionRepository quizQuestionRepository

    @Autowired
    QuestionAnswerRepository questionAnswerRepository

    @Autowired
    ClarificationRequestRepository clarificationRequestRepository

    @Autowired
    ClarificationCommentRepository clarificationCommentRepository

    @Shared
    def user

    def clarificationRequest
    def clarificationCommentDto

    def setup() {
        def course = new Course()
        course.setName("course")
        courseRepository.save(course)

        def courseExecution = new CourseExecution()
        courseExecution.setCourse(course)
        courseExecutionRepository.save(courseExecution)

        user = new User("Name", "Username", 1, User.Role.TEACHER)
        user.addCourse(courseExecution)
        courseExecution.addUser(user)
        userRepository.save(user)

        def quiz = new Quiz()
        quiz.setTitle("titleQuiz")
        quiz.setKey(1)
        quiz.setType(Quiz.QuizType.GENERATED.toString())
        quiz.setCourseExecution(courseExecution)
        quizRepository.save(quiz)

        def question = new Question()
        question.setTitle("titleQuestion")
        question.setKey(1)
        questionRepository.save(question)

        def quizQuestion = new QuizQuestion(quiz, question, 0)
        quizQuestionRepository.save(quizQuestion)

        def quizAnswer = new QuizAnswer()
        quizAnswer.setQuiz(quiz)
        quizAnswerRepository.save(quizAnswer)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setQuizAnswer(quizAnswer)
        questionAnswer.setQuizQuestion(quizQuestion)
        questionAnswerRepository.save(questionAnswer)

        def student = new User("Student", "Student", 2, User.Role.STUDENT)
        student.addCourse(courseExecution)
        courseExecution.addUser(student)
        userRepository.save(student)

        clarificationRequest = new ClarificationRequest()
        clarificationRequest.setState(ClarificationRequest.State.UNRESOLVED)
        clarificationRequest.setContent(CLARIFICATION_CONTENT)
        clarificationRequest.setUser(student)
        clarificationRequest.setQuestionAnswer(questionAnswer)
        clarificationRequestRepository.save(clarificationRequest)

        def creationDate = DateHandler.now()

        clarificationCommentDto = new ClarificationCommentDto()
        clarificationCommentDto.setContent(COMMENT_CONTENT)
        clarificationCommentDto.setUser(new UserDto(user))
        clarificationCommentDto.setCreationDate(DateHandler.toISOString(creationDate))
    }

    def "submit a comment to a clarification request"() {
        when:
        clarificationCommentService.createClarificationComment(clarificationRequest.getId(), clarificationCommentDto)

        then: "the comment data is correct"
        def comment = clarificationCommentRepository.findComment(clarificationRequest.getId())
        comment.getId() != null
        comment.getContent() == clarificationCommentDto.getContent()
        comment.getUser().getUsername() == user.getUsername()
        DateHandler.toISOString(comment.getCreationDate()) == clarificationCommentDto.getCreationDate()
    }

    def "submit a comment without a creationTime"() {
        given: "Update commentDto"
        clarificationCommentDto.setCreationDate(null)

        when:
        clarificationCommentService.createClarificationComment(clarificationRequest.getId(), clarificationCommentDto)

        then:
        def comment = clarificationCommentRepository.findComment(clarificationRequest.getId())
        comment.getCreationDate() != null
    }

    @Unroll("Test: #clarificationId")
    def "submit comment to an non existing clarification request"() {
        given: "update clarification request"
        clarificationRequest.setId(clarificationId)
        when:
        clarificationCommentService.createClarificationComment(clarificationRequest.getId(), clarificationCommentDto)

        then:
        def error = thrown(TutorException)
        error.getErrorMessage() == ErrorMessage.COMMENT_INVALID_CLARIFICATION

        where:
        clarificationId << [500, 0]
    }

    @Unroll("Test: #content | clarificationRequestState || #message")
    def "submit comment with wrong arguments"() {
        given: "Update commentDto"
        clarificationCommentDto.setContent(content)
        and: "ClarificationRequest update"
        clarificationRequest.setState(clarificationRequestState)

        when:
        clarificationCommentService.createClarificationComment(clarificationRequest.getId(), clarificationCommentDto)

        then:
        def error = thrown(TutorException)
        error.getErrorMessage() == message

        where:
            content     |       clarificationRequestState       ||         message
        null            | ClarificationRequest.State.UNRESOLVED || ErrorMessage.COMMENT_INVALID_CONTENT
        COMMENT_CONTENT | null                                  || ErrorMessage.COMMENT_INVALID_CLARIFICATION_STATE
        COMMENT_CONTENT | ClarificationRequest.State.RESOLVED   || ErrorMessage.COMMENT_INVALID_CLARIFICATION_STATE
    }

    def "submit an empty comment"() {
        when:
        clarificationCommentService.createClarificationComment(clarificationRequest.getId(), null)

        then:
        def error = thrown(TutorException)
        error.getErrorMessage() == ErrorMessage.COMMENT_IS_EMPTY
    }

    def "student tries to submit comment to clarification request"() {
        given: "another user"
        def newUser = new User("Name2", "Username2", 3, User.Role.STUDENT)
        userRepository.save(newUser)
        and: "update clarification request"
        clarificationRequest.setUser(newUser)
        and: "update commentDto"
        clarificationCommentDto.setUser(new UserDto(newUser))

        when:
        clarificationCommentService.createClarificationComment(clarificationRequest.getId(), clarificationCommentDto)

        then:
        def error = thrown(TutorException)
        error.getErrorMessage() == ErrorMessage.COMMENT_INVALID_USER
    }

    def "teacher tries to submits comment to clarification request from different course"() {
        given: "A course"
        def courseUser = new Course()
        courseUser.setName("firstCourse")
        courseRepository.save(courseUser)
        and: "courseExecution"
        def courseExecUser = new CourseExecution()
        courseExecUser.setCourse(courseUser)
        courseExecutionRepository.save(courseExecUser)
        and: "Update user with course"
        Set<CourseExecution> courseExecutions = new HashSet<>()
        courseExecutions.add(courseExecUser)
        user.setCourseExecutions(courseExecutions)

        when:
        clarificationCommentService.createClarificationComment(clarificationRequest.getId(), clarificationCommentDto)

        then:
        def error = thrown(TutorException)
        error.getErrorMessage() == ErrorMessage.COMMENT_INVALID_USER_COURSE
    }

    @TestConfiguration
    static class SubmitCommentTestContextConfiguration {

        @Bean
        ClarificationCommentService commentService() {
            return new ClarificationCommentService()
        }
    }
}