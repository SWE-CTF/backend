package sogong.ctf.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Question extends Post {
    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;

    @OneToOne
    @JoinColumn(name = "adopted_comment")
    private Comment adoptedComment;

    @Builder
    public Question(String title, String content, Challenge challengeId, Member memberId, LocalDateTime writeTime) {
        super(title, content, writeTime);
        this.challengeId = challengeId;
        this.memberId = memberId;
        this.adoptedComment = null;
    }


    public void updateQuestion(String title, String content) {
        setTitle(title);
        setContent(content);
    }

    public void adopt(Comment comment) {
        this.adoptedComment = comment;
    }
}
