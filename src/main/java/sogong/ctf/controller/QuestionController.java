package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.QuestionResponseDTO;
import sogong.ctf.dto.QuestionSaveDTO;
import sogong.ctf.service.ChallengeService;
import sogong.ctf.service.MemberService;
import sogong.ctf.service.QuestionService;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/question")
public class QuestionController {
    private final ChallengeService challengeService;
    private final QuestionService questionService;
    private final MemberService memberService;

    @PostMapping("/save")//질문 게시글 작성
    public ResponseEntity save(@RequestBody QuestionSaveDTO saveForm, HttpSession session) {//질문 작성
        //세션으로부터 사용자 정보 가져오기
        long memberId = (long) session.getAttribute("JSESSIONID");
        Member member = memberService.findOne(memberId).orElseThrow();

        Challenge challenge = challengeService.findByChallengeId(saveForm.getChallengeId());

        try {
            questionService.save(saveForm, challenge, member);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{questionId}") //질문 게시글 상세조회
    public ResponseEntity<QuestionResponseDTO> getQuestion(@RequestParam("questionId") long questionId) {
        QuestionResponseDTO question = questionService.findOne(questionId);
        if (question != null) {
            return ResponseEntity.ok(question);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @DeleteMapping("/{questionId}")
    public ResponseEntity deleteQuestion(@RequestParam("questionId")long questionId){
        //글 작성자와 지우려고 하는 사람 일치 여부 확인
        return null;
    }
    @PutMapping("/{questionId}")
    public ResponseEntity updateQuestion(@RequestParam("questionId")long questionId){
        return null;
    }


}
