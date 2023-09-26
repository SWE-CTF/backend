package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Team;

public interface TeamRepository extends JpaRepository<Team,Long> {
}
