package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.CommentResponseDTO;
import sogong.ctf.dto.QuestionPagingDTO;
import sogong.ctf.dto.QuestionResponseDTO;
import sogong.ctf.dto.QuestionSaveDTO;
import sogong.ctf.service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @PostMapping("/save")//질문 게시글 작성
    public ResponseEntity saveQuestion(@RequestBody QuestionSaveDTO saveForm, @AuthUser Member member) {//질문 작성
        Optional<Challenge> findChallenge = challengeService.findByChallengeId(saveForm.getChallengeId());//문제 번호 확인
        if (findChallenge.isEmpty())
            return ResponseEntity.status(404).build();//해당 문제 번호 없을 경우
        else if (member.getId() == null) {//인증된 사용자 아닐 경우
            return ResponseEntity.status(403).build();
        }
        try {
            questionService.save(member, saveForm, findChallenge.get());//질문 저장
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{questionId}") //질문 게시글 상세조회
    public ResponseEntity<QuestionResponseDTO> getQuestion(@PathVariable("questionId") long questionId) {
        QuestionResponseDTO question = questionService.getDetails(questionId);
        if (question != null) {
            List<CommentResponseDTO> commentList = commentService.getComments(questionId);
            question.setCommentList(commentList);
            return ResponseEntity.ok(question);
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity deleteQuestion(@PathVariable("questionId") long questionId, @AuthUser Member member) {
        Member writer = questionService.findWriter(questionId);
        if (memberService.IsEquals(member, writer)) {//글 작성자와 지우려고 하는 사람 일치 여부 확인
            questionService.delete(questionId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{questionId}")
    public ResponseEntity updateQuestion(@PathVariable("questionId") long questionId, @RequestBody QuestionSaveDTO questionSaveDTO, @AuthUser Member member) {
        Member writer = questionService.findWriter(questionId);
        if (memberService.IsEquals(member, writer)) {
            questionService.update(questionId, questionSaveDTO);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/paging")
    public ResponseEntity<Map<String, Object>> paging(@PageableDefault(page = 1) Pageable page) {
        List<QuestionPagingDTO> paging = questionService.paging(page);
        if (paging.size() == 0) {
            return ResponseEntity.status(404).build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("paging", paging);
        return ResponseEntity.ok(response);
    }

}
