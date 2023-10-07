package sogong.ctf.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    /*
    @OneToOne
    @JoinColumn(name = "scoreboard_id")
    private Scoreboard scoreboard;


     */
    @Builder
    public Team(String name){
        this.name = name;
    }

    /*
    public void setScoreboard(Scoreboard scoreboard){
        this.scoreboard = scoreboard;
    }

     */
}
