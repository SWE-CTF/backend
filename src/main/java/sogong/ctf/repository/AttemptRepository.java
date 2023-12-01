package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Attempt;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
}
