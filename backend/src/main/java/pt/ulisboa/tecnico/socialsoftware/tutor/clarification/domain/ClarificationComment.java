package pt.ulisboa.tecnico.socialsoftware.tutor.clarification.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.dto.ClarificationCommentDto;

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Comments")
public class ClarificationComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "clarification_id")
    private Clarification clarification;

    private LocalDateTime creationDate;

    public ClarificationComment() {}

    public ClarificationComment(ClarificationCommentDto clarificationCommentDto, User user, Clarification clarification) {

        if (clarificationCommentDto.getContent() == null) {
            throw new TutorException(ErrorMessage.COMMENT_INVALID_CONTENT);
        }

        this.content = clarificationCommentDto.getContent();
        this.user = user;
        this.clarification = clarification;
        this.creationDate = clarificationCommentDto.getCreationDate() == null ?
                            LocalDateTime.now() : clarificationCommentDto.getCreationDate();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Clarification getClarification() {
        return clarification;
    }

    public void setClarification(Clarification clarification) {
        this.clarification = clarification;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}