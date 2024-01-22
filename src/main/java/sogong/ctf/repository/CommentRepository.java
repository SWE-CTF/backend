package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Comment;
import sogong.ctf.domain.Question;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByQuestionId(Question question);

    boolean existsByMemberIdAndId(Long memberId, Long id);

}
