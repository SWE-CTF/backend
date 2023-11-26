package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.ChallengeFile;

import java.util.List;

public interface ChallengeFileRepository extends JpaRepository<ChallengeFile, Long> {
    List<ChallengeFile> findAllByChallengeId(Challenge challenge);

    void deleteAllByChallengeId(Challenge challenge);
}
