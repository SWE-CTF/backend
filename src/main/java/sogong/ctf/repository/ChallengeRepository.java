package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Category;
import sogong.ctf.domain.Challenge;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
}
