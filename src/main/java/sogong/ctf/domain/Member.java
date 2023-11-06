package sogong.ctf.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private String name;
    private String email;
    private String nickname;

    @ColumnDefault("0")
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "memberId")
    private List<Attempt> attempts = new ArrayList<>();

    @Builder
    public Member(String username,String password, String name, String email, String nickname,  Role role){
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }

    public void joinTeam(Team team){
        this.team = team;
    }

    public void addAttempt(Attempt attempt){this.attempts.add(attempt);}

}
