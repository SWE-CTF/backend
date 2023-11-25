package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.TestCase;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findAllByChallengeId(Challenge challenge);

    void deleteAllByChallengeId(Challenge challenge);
}
