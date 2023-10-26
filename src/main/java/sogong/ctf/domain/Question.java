package sogong.ctf.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Question {
    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private long id;
    private String title;
    private String content;
    private LocalDateTime writeTime;
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
        this.title = title;
        this.content = content;
        this.challengeId = challengeId;
        this.memberId = memberId;
        this.writeTime = writeTime;
        this.adoptedComment = null;
    }

    public void updateQuestion(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void adopt(Comment comment) {
        this.adoptedComment = comment;
    }
}
