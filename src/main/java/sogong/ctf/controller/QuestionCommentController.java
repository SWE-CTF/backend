package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.QuestionCommentSaveDTO;
import sogong.ctf.service.MemberService;
import sogong.ctf.service.QuestionCommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comment")
public class QuestionCommentController {
    private final QuestionCommentService commentService;
    private final MemberService memberService;

    @PostMapping("/{questionId}/save")
    public ResponseEntity saveComment(@PathVariable("questionId") long questionId, @RequestBody QuestionCommentSaveDTO request, Member member) {
        try {
            commentService.save(member, questionId, request.getContent());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable("commentId") long commentId, @RequestBody QuestionCommentSaveDTO request, Member member) {
        Member writer = commentService.findWriter(commentId);
        if (memberService.IsEquals(member, writer)) {
            commentService.update(commentId, request.getContent());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") long commentId, Member member) {
        Member writer = commentService.findWriter(commentId);
        if (memberService.IsEquals(member, writer)) {
            commentService.delete(commentId);
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
