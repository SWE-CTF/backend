package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.Comment;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Question;
import sogong.ctf.dto.CommentResponseDTO;
import sogong.ctf.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionCommentService {
    private final CommentRepository commentRepository;
    private final QuestionService questionService;

    @Transactional
    public long save(Member member, long questionId, String content) {

        Optional<Question> question = questionService.findOne(questionId);

        if (question.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Comment comment = Comment.builder()
                .writer(member)
                .content(content)
                .writeTime(LocalDateTime.now())
                .questionId(question.get())
                .build();
        Comment save = commentRepository.save(comment);

        return save.getId();
    }
    public List<CommentResponseDTO> getComments(long questionId){
        Question question = questionService.findOne(questionId).get();
        List<Comment> commentList = commentRepository.findAllByQuestionId(question);
        List<CommentResponseDTO> dtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            dtoList.add(CommentResponseDTO.toCommentResponseDTO(comment));
        }
        return dtoList;
    }

    public Comment findOne(long commentId){
        return commentRepository.findById(commentId).get();
    }
    public Member findWriter(long commentId) {
        return findOne(commentId).getWriter();
    }

    @Transactional
    public void update(long commentId, String content) {
        Comment comment = findOne(commentId);
        comment.updateComment(content);
    }

    @Transactional
    public void delete(long commentId) {
        Comment comment = findOne(commentId);
        commentRepository.delete(comment);
    }

    @Transactional
    public void adopt(long commentId) {
        Comment comment = findOne(commentId);
        comment.getQuestionId().adopt(comment);
    }
}
