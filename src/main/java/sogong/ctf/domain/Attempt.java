package sogong.ctf.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attempt {
    @Id
    @GeneratedValue
    @Column(name = "Attempt_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    private String code;
    private float memory;
    private float time;

    @Enumerated(EnumType.STRING)
    private CodeStatus codeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;

    @Builder
    public Attempt(Member member, String code, float memory, float time, CodeStatus codeStatus, Challenge challenge){
        this.memberId = member;
        this.code =code;
        this.memory = memory;
        this.time = time;
        this.codeStatus = codeStatus;
        this.challengeId = challenge;
    }

    public void updateStatus(CodeStatus codeStatus){
        this.codeStatus = codeStatus;
    }

}
