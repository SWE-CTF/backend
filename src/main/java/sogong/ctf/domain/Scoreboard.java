package sogong.ctf.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scoreboard {

    @Id @GeneratedValue
    @Column(name = "scoreboard_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Team team;

    private int score;

    @Builder
    public Scoreboard(Team team, int score) {
        this.team = team;
        this.score = score;
    }
}
