package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.request.QuestionSaveDTO;
import sogong.ctf.dto.response.CommentResponseDTO;
import sogong.ctf.dto.response.QuestionPagingDTO;
import sogong.ctf.dto.response.QuestionResponseDTO;
import sogong.ctf.service.AuthUser;
import sogong.ctf.service.ChallengeService;
import sogong.ctf.service.QuestionCommentService;
import sogong.ctf.service.QuestionService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/question")
public class QuestionController {
    private final ChallengeService challengeService;
    private final QuestionService questionService;
    private final QuestionCommentService commentService;

    /*
    질문 게시글 작성
     */
    @PostMapping("/save")
    public ResponseEntity saveQuestion(@RequestBody @Valid QuestionSaveDTO saveForm, @AuthUser Member member) { // 질문 작성
        Challenge findChallenge = challengeService.findByChallengeId(saveForm.getChallengeId()); // 문제 번호 확인
        questionService.save(member, saveForm, findChallenge); // 질문 저장
        return ResponseEntity.ok().build();
    }

    /*
    문제 별 질문게시글 조회
     */
    @GetMapping("/challenge/{challengeId}")
    public ResponseEntity<List<QuestionPagingDTO>> getQuestionByChallengeId(@PathVariable("challengeId") Long challengeId) {
        List<QuestionPagingDTO> questions = questionService.getQuestionsByChallengeId(challengeId);
        if (questions.size() == 0)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(questions);
    }

    /*
    질문 게시글 상세조회
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponseDTO> getQuestion(@PathVariable("questionId") Long questionId) {
        QuestionResponseDTO question = questionService.getDetails(questionId);
        if (question != null) {
            List<CommentResponseDTO> commentList = commentService.getComments(questionId);
            question.setCommentList(commentList);
            return ResponseEntity.ok(question);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
    질문 게시글 삭제
     */
    @DeleteMapping("/{questionId}")
    public ResponseEntity deleteQuestion(@PathVariable("questionId") Long questionId, @AuthUser Member member) {
        questionService.validateQuestionByMember(member.getId(), questionId);
        questionService.delete(questionId);
        return ResponseEntity.ok().build();
    }

    /*
    질문 게시글 수정
     */
    @PutMapping("/{questionId}")
    public ResponseEntity updateQuestion(@PathVariable("questionId") Long questionId, @RequestBody QuestionSaveDTO questionupdateDTO, @AuthUser Member member) {
        questionService.validateQuestionByMember(member.getId(), questionId);
        questionService.update(questionId, questionupdateDTO);
        return ResponseEntity.ok().build();
    }

    /*
    질문 게시글 페이징
     */
    @GetMapping("/paging")
    public ResponseEntity<List<QuestionPagingDTO>> AllPage() {
        List<QuestionPagingDTO> list = questionService.getAllQuestion();
        return ResponseEntity.ok(list);
    }
}
