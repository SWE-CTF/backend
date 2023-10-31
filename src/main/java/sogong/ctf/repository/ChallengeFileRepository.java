package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.ChallengeFile;

public interface ChallengeFileRepository extends JpaRepository<ChallengeFile,Long> {
}
