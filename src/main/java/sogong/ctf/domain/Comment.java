package sogong.ctf.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private long id;
    private String content;
    private LocalDateTime writeTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Question questionId;

    @Builder
    public Comment(String content, LocalDateTime writeTime, Member writer, Question questionId) {
        this.content = content;
        this.writeTime = writeTime;
        this.writer = writer;
        this.questionId = questionId;
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
