package sogong.ctf.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class TestCase {
    @Id @GeneratedValue
    @Column(name = "case_id")
    private long id;

    private String input;
    private String output;
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;

}
