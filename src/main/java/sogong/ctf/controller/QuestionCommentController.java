package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.request.QuestionCommentSaveDTO;
import sogong.ctf.service.QuestionCommentService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comment")
@Slf4j
public class QuestionCommentController {
    private final QuestionCommentService commentService;

    @PostMapping("/{questionId}/save")
    public ResponseEntity saveComment(@PathVariable("questionId") long questionId, @RequestBody QuestionCommentSaveDTO request, Member member) {
        log.info("댓글 작성 요청");
        try {
            long save = commentService.save(member, questionId, request.getContent());
            Map<String, Long> result = new HashMap<>();
            result.put("commentId", save);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable("commentId") long commentId, @RequestBody QuestionCommentSaveDTO request, Member member) {
        if (commentService.update(commentId, request.getContent(), member)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") long commentId, Member member) {
        if (commentService.delete(commentId, member)) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/{commentId}/adopt")
    public ResponseEntity adoptComment(@PathVariable("commentId") long commentId) {
        commentService.adopt(commentId);
        return ResponseEntity.status(200).build();
    }
}
