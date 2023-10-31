package sogong.ctf.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class ChallengeFile {
    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private long id;
    private String originalFileName;
    private String storedFileName;
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;

    @Builder
    public ChallengeFile(long id, String originalFileName, String storedFileName, Challenge challengeId) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.challengeId = challengeId;
    }
}
