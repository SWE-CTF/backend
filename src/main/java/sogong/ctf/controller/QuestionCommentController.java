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
    public ResponseEntity saveComment(@PathVariable("questionId") long questionId, @RequestBody QuestionCommentSaveDTO request, @AuthUser Member member) {
        long save = commentService.save(member, questionId, request.getContent());

        Map<String, Long> result = new HashMap<>();
        result.put("commentId", save);
        return ResponseEntity.ok(result);
    }

    /*
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable("commentId") long commentId, @RequestBody QuestionCommentSaveDTO request, @AuthUser Member member) {
        commentService.update(commentId, request.getContent(), member);
        return ResponseEntity.ok().build();
    }

    /*
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") long commentId, @AuthUser Member member) {
        commentService.delete(commentId, member);
        return ResponseEntity.status(204).build();
    }

    /*
     * 댓글 채택
     */
    @PostMapping("/{commentId}/adopt")
    public ResponseEntity adoptComment(@PathVariable("commentId") long commentId, @AuthUser Member member) {
        commentService.adopt(commentId, member);
        return ResponseEntity.ok().build();
    }
}
