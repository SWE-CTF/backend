package sogong.ctf.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class TestCase {
    @Id
    @GeneratedValue
    @Column(name = "case_id")
    private Long id;

    @Column(columnDefinition = "Text")
    private String input;
    @Column(columnDefinition = "Text")
    private String output;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;

    @Builder
    public TestCase(String input, String output, Challenge challengeId) {
        this.input = input;
        this.output = output;
        this.challengeId = challengeId;
    }
}
