package pt.ulisboa.tecnico.socialsoftware.tutor.studentQuestion;

import org.springframework.beans.factory.annotation.Autowired;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_NOT_FOUND;

public class StudentQuestionService {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    StudentQuestionRepository studentQuestionRepository;

    @PersistenceContext
    EntityManager entityManager;

    public StudentQuestionDto createStudentQuestion(String username, int courseId, QuestionDto questionDto) {

        if (questionDto == null)
            throw new TutorException(ErrorMessage.QUESTION_IS_EMPTY);

        User user = userService.findByUsername(username);

        QuestionDto createdQuestionDto = questionService.createQuestion(courseId, questionDto);
        int questionId = createdQuestionDto.getId();
        questionService.questionSetStatus(questionId, Question.Status.PENDING);

        // TODO: Verify errors
        Question question = questionRepository.findById(questionId).get();

        StudentQuestion studentQuestion = new StudentQuestion(user, question);
        this.entityManager.persist(studentQuestion);
        return new StudentQuestionDto(studentQuestion);
    }


}