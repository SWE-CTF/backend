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
    @Id @GeneratedValue
    @Column(name="question_id")
    private long id;
    private String title;
    private String content;
    private LocalDateTime writeTime;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

    @ManyToOne
    @JoinColumn(name="challenge_id")
    private Challenge challengeId;

    @Builder
    public Question(String title, String content, Challenge challengeId, Member memberId) {
        this.title = title;
        this.content = content;
        this.challengeId = challengeId;
        this.memberId = memberId;
        this.writeTime =LocalDateTime.now();
    }
}
