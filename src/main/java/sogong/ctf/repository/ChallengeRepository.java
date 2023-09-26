package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.ctf.domain.Challenge;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
