package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.config.security.CustomMemberDetails;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.QuestionResponseDTO;
import sogong.ctf.dto.QuestionSaveDTO;
import sogong.ctf.service.ChallengeService;
import sogong.ctf.service.MemberService;
import sogong.ctf.service.QuestionService;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/question")
public class QuestionController {
    private final ChallengeService challengeService;
    private final QuestionService questionService;
    private final MemberService memberService;

    @PostMapping("/save")//질문 게시글 작성
    public ResponseEntity save(@RequestBody QuestionSaveDTO saveForm) {//질문 작성
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomMemberDetails findMember = (CustomMemberDetails) authentication.getPrincipal();
        Member member = findMember.getMember();//사용자 확인

        Optional<Challenge> findChallenge = challengeService.findByChallengeId(saveForm.getChallengeId());//문제 번호 확인
        if (findChallenge.isEmpty())
            return ResponseEntity.notFound().build();//해당 문제 번호 없을 경우

        try {
            questionService.save(saveForm, findChallenge.get(), member);//질문 저장
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{questionId}") //질문 게시글 상세조회
    public ResponseEntity<QuestionResponseDTO> getQuestion(@PathVariable("questionId") long questionId) {
        QuestionResponseDTO question = questionService.findQuestion(questionId);
        if (question != null) {
            return ResponseEntity.ok(question);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity deleteQuestion(@RequestParam("questionId") long questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomMemberDetails findMember = (CustomMemberDetails) authentication.getPrincipal();
        Member member = findMember.getMember();

        Member writer = questionService.findWriter(questionId);
        if (member.equals(writer)) {
            questionService.delete(questionId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
        //글 작성자와 지우려고 하는 사람 일치 여부 확인

    }

    @PutMapping("/{questionId}")
    public ResponseEntity updateQuestion(@PathVariable("questionId") long questionId, @RequestBody QuestionSaveDTO questionSaveDTO) {
        questionService.put(questionId, questionSaveDTO);
        return null;
    }


}
