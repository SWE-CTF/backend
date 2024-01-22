package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.request.QuestionCommentSaveDTO;
import sogong.ctf.service.AuthUser;
import sogong.ctf.service.QuestionCommentService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comment")
public class QuestionCommentController {
    private final QuestionCommentService commentService;

    /*
     * 댓글 작성
     */
    @PostMapping("/{questionId}/save")
    public ResponseEntity saveComment(@PathVariable("questionId") Long questionId,
                                      @RequestBody QuestionCommentSaveDTO request,
                                      @AuthUser Member member) {
        long save = commentService.save(member, questionId, request.getContent());

        Map<String, Long> result = new HashMap<>();
        result.put("commentId", save);
        return ResponseEntity.ok(result);
    }

    /*
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable("commentId") Long commentId,
                                        @RequestBody QuestionCommentSaveDTO request,
                                        @AuthUser Member member) {
        commentService.validateMemberByCommentId(member.getId(), commentId);
        commentService.update(commentId, request.getContent());
        return ResponseEntity.ok().build();
    }

    /*
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId,
                                        @AuthUser Member member) {
        commentService.validateMemberByCommentId(member.getId(), commentId);
        commentService.delete(commentId);
        return ResponseEntity.status(204).build();
    }

    /*
     * 댓글 채택
     */
    @PostMapping("/{commentId}/adopt")
    public ResponseEntity adoptComment(@PathVariable("commentId") Long commentId,
                                       @AuthUser Member member) {
        commentService.validateMemberByCommentId(member.getId(), commentId);
        commentService.adopt(commentId);
        return ResponseEntity.ok().build();
    }
}
