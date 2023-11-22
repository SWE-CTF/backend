package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.request.CodeRequestDTO;
import sogong.ctf.service.AttemptService;
import sogong.ctf.service.AuthUser;

@RestController
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

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
    public ResponseEntity getChallengeAttempts(@PathVariable("challengeId") Long challengeId){
        try{
            return ResponseEntity.ok(attemptService.getChallengeAttempt(challengeId));
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
