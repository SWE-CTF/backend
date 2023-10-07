package sogong.ctf.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@RequiredArgsConstructor
public class Comment {
    @Id @GeneratedValue
    private long commentId;
    private String content;
    private LocalDateTime writeTime;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member writer;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question questionId;

}
