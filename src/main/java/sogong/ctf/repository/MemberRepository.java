package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sogong.ctf.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    @Query("SELECT t FROM Member t ORDER BY t.count DESC")
    List<Member> findAllOrderByCount();

    Optional<Member> findByNickname(String nickname);
}
