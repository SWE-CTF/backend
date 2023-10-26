package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Scoreboard;


public interface ScoreboardRepository extends JpaRepository<Scoreboard, Long> {
}

