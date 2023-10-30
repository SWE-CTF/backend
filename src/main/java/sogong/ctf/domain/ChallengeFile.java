package sogong.ctf.domain;

import javax.persistence.*;

@Entity
public class ChallengeFile {
    @Id
    @GeneratedValue
    private long id;
    private String originalFileName;
    private String storedFileName;
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;
}
