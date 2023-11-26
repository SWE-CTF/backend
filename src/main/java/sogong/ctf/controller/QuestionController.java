package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.response.CommentResponseDTO;
import sogong.ctf.dto.response.QuestionPagingDTO;
import sogong.ctf.dto.response.QuestionResponseDTO;
import sogong.ctf.dto.request.QuestionSaveDTO;
import sogong.ctf.service.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/question")
public class QuestionController {
    private final ChallengeService challengeService;
    private final QuestionService questionService;
    private final QuestionCommentService commentService;
    private final MemberService memberService;

    /*
    질문 게시글 작성
     */
    @PostMapping("/save")
    public ResponseEntity saveQuestion(@RequestBody @Valid QuestionSaveDTO saveForm, @AuthUser Member member) {//질문 작성
        log.info("질문 게시글 작성 요청");
        if (member.getId() == null) {
            return ResponseEntity.status(403).build();
        } else {
            Optional<Challenge> findChallenge = challengeService.findByChallengeId(saveForm.getChallengeId());//문제 번호 확인
            if (findChallenge.isEmpty())
                return ResponseEntity.notFound().build();//해당 문제 번호 없을 경우
            try {
                questionService.save(member, saveForm, findChallenge.get());//질문 저장
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                log.error(e.getMessage());
                return ResponseEntity.internalServerError().build();
            }
        }
    }

    /*
    문제 별 질문게시글 조회
     */
    @GetMapping("/challenge/{challengeId}")
    public ResponseEntity<List<QuestionPagingDTO>> getQuestionByChallengeId(@PathVariable("challengeId") int challengeId) {
        List<QuestionPagingDTO> questions =questionService.getQuestionsByChallengeId(challengeId);
        if(questions.size()==0)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(questions);
    }

    /*
    질문 게시글 상세조회
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponseDTO> getQuestion(@PathVariable("questionId") int questionId) {
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
    public ResponseEntity deleteQuestion(@PathVariable("questionId") int questionId, @AuthUser Member member) {
        try {
            if (questionService.delete(questionId, member)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(403).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
    질문 게시글 수정
     */
    @PutMapping("/{questionId}")
    public ResponseEntity updateQuestion(@PathVariable("questionId") int questionId, @RequestBody QuestionSaveDTO questionupdateDTO, @AuthUser Member member) {
        try {
            if (questionService.update(questionId, questionupdateDTO, member)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(403).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
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
