package pt.ulisboa.tecnico.socialsoftware.tutor.studentQuestion;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import javax.persistence.*;

@Entity
@Table(name = "student_questions")
public class StudentQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public StudentQuestion() {

    }

    public StudentQuestion(User user, Question question) {
        this.student = user;
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public User getStudent() {
        return student;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
