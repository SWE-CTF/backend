package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.CodeRequestDTO;
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

    @GetMapping("/api/attempt/challenge")
    public ResponseEntity getChallengeAttempts(){
        //challenge id 받아오기
        Long id = (long) 0.0; // 나중에 고칠거
        try{
            return ResponseEntity.ok(attemptService.getChallengeAttempt(id));
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
