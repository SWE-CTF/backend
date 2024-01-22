package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    boolean existsByMemberIdAndId(Long member, Long id);
}
