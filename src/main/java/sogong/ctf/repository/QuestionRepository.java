package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    public List<Question> findAllByChallengeId(Challenge challengeId);
}
