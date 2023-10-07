package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
