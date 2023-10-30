package sogong.ctf.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sogong.ctf.domain.Team;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ScoreboardRepositoryTest {

    @Autowired
    private ScoreboardRepository scoreboardRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void teamScoreInsertSuccess(){

        //given
        Team team = Team.builder()
                .name("Test")
                .build();

        Scoreboard scoreboard = Scoreboard.builder()
                .team(team)
                .score(100)
                .build();

        team.setScoreboard(scoreboard);

        Long team_id = teamRepository.save(team).getId();
        scoreboardRepository.save(scoreboard);

        //when

       Optional<Team> find_team = teamRepository.findById(team_id);

        //then
        assertNotNull(find_team.get());
        assertEquals(100,find_team.get().getScoreboard().getScore());

    }
}