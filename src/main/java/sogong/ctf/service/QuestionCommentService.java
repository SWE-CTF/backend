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
import sogong.ctf.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionCommentService {
    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final QuestionService questionService;

    @Transactional
    public long save(Member member, long questionId, String content) {

        Question question = questionService.findByQuestionId(questionId);

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
        Question question = questionService.findByQuestionId(questionId);
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

    private Member findWriter(long commentId) {
        return findByCommentId(commentId).getWriter();
    }

    @Transactional
    public void update(long commentId, String content, Member member) {
        Member writer = findWriter(commentId);
        if (memberService.IsEquals(member, writer)) {
            Comment comment = findByCommentId(commentId);
            comment.updateComment(content);
        } else throw new AccessDeniedException("댓글 작성자와 사용자가 일치하지 않습니다.");
    }

    @Transactional
    public void delete(long commentId, Member member) {
        Member writer = findWriter(commentId);
        if (memberService.IsEquals(member, writer)) {
            Comment comment = findByCommentId(commentId);
            commentRepository.delete(comment);
        } else throw new AccessDeniedException("댓글 작성자와 사용자가 일치하지 않습니다.");
    }

    @Transactional
    public void adopt(long commentId, Member member) {
        Member writer = findWriter(commentId);
        if (memberService.IsEquals(member, writer)) {
            Comment comment = findByCommentId(commentId);
            comment.getQuestionId().adopt(comment);
        } else throw new AccessDeniedException("댓글 작성자와 사용자가 일치하지 않습니다.");
    }
}
