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
    @ManyToOne
    @JoinColumn(name = "writer")
    private Member writer;
    @ManyToOne
    @JoinColumn(name = "question_id")
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
