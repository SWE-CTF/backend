package sogong.ctf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.dto.QuestionCommentDTO;

@RestController
@RequestMapping("api/question/comment")
public class QuestionCommentController {

    @PostMapping("{questionId}/save")
    public ResponseEntity save(@PathVariable("questionId") long questionId, @RequestBody QuestionCommentDTO questionCommentDTO) {
        return null;
    }
}
