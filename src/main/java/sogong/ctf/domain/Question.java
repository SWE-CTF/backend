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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopted_comment")
    private Comment adoptedComment;

    @Builder
    public Question(String title, String content, Challenge challenge, Member memberId, LocalDateTime writeTime) {
        super(title, content, writeTime, memberId);
        this.challenge = challenge;
        this.adoptedComment = null;
    }

    public void updateQuestion(String title, String content) {
        updateTitle(title);
        updateContent(content);
    }

    public void adopt(Comment comment) {
        this.adoptedComment = comment;
    }
}
