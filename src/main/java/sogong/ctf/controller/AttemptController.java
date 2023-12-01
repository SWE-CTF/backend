package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.request.CodeRequestDTO;
import sogong.ctf.service.AttemptService;
import sogong.ctf.service.AuthUser;
import sogong.ctf.service.MemberService;

@RestController
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;
    private final MemberService memberService;

    @PostMapping("/api/attempt/challenge")
    public ResponseEntity compileCode(@AuthUser Member member, @RequestBody CodeRequestDTO codeRequestDTO){
        try{
            attemptService.compileAndRun(codeRequestDTO,member);
            return new ResponseEntity(HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/api/attempt/member")
    public ResponseEntity getAttempts(@AuthUser Member member){
        try{
            return ResponseEntity.ok(attemptService.getMemberAttempt(member));
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/api/challenge/{challengeId}/attempt")
    public ResponseEntity getChallengeAttempts(@PathVariable("challengeId") int challengeId){
        try{
            return ResponseEntity.ok(attemptService.getChallengeAttempt(challengeId));
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/challenge/{challengeId}/member")
    public ResponseEntity getChallengeAttemptbyMember(@PathVariable("challengeId") int challengeId,@AuthUser Member member){
        try{
            return ResponseEntity.ok(memberService.showAllChallenge(member, challengeId));
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/attempt/member/success")
    public ResponseEntity getChallengeSuccess(@AuthUser Member member){
        try {
            return ResponseEntity.ok(attemptService.getChallengeSuccess(member));
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }
}
