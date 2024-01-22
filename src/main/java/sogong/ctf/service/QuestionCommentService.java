package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.Comment;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Question;
import sogong.ctf.dto.response.CommentResponseDTO;
import sogong.ctf.exception.CommentNotFoundException;
import sogong.ctf.exception.ErrorCode;
import sogong.ctf.exception.QuestionNotFoundException;
import sogong.ctf.repository.CommentRepository;
import sogong.ctf.repository.QuestionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionCommentService {
    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public long save(Member member, Long questionId, String content) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(ErrorCode.QUESTION_NOT_EXIST));

        Comment comment = Comment.builder()
                .writer(member)
                .content(content)
                .writeTime(LocalDateTime.now())
                .questionId(question)
                .build();
        Comment save = commentRepository.save(comment);

        return save.getId();
    }

    public List<CommentResponseDTO> getComments(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(ErrorCode.QUESTION_NOT_EXIST));

        List<Comment> commentList = commentRepository.findAllByQuestionId(question);
        return commentList.stream().map(comment -> CommentResponseDTO.builder()
                .commentId(comment.getId())
                .nickname(comment.getWriter().getNickname())
                .content(comment.getContent())
                .writeTime(comment.getWriteTime())
                .build()
        ).collect(Collectors.toList());
    }

    public Comment findByCommentId(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(ErrorCode.COMMENT_NOT_EXIST));
    }

    public void validateMemberByCommentId(Long memberId, Long noticeId) {
        if (!commentRepository.existsByMemberIdAndId(memberId, noticeId))
            throw new AccessDeniedException("댓글 작성자와 사용자가 일치하지 않습니다.");
    }

    @Transactional
    public void update(long commentId, String content) {
        Comment comment = findByCommentId(commentId);
        comment.updateComment(content);
    }

    @Transactional
    public void delete(long commentId) {
        Comment comment = findByCommentId(commentId);
        commentRepository.delete(comment);
    }

    @Transactional
    public void adopt(long commentId) {
        Comment comment = findByCommentId(commentId);
        comment.getQuestionId().adopt(comment);
    }
}
