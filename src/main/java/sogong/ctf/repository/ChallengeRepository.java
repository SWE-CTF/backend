package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogong.ctf.domain.Category;
import sogong.ctf.domain.Challenge;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    public List<Challenge> findAllByCategoryId(Category category);

    public List<Challenge> findAllByTitleContaining(String keyword);
    public boolean existsByMemberIdAndId(Long member,Long id);
}
