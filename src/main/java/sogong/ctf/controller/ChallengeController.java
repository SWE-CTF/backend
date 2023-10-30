package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.ctf.dto.ChallengePagingDTO;
import sogong.ctf.service.AttemptService;
import sogong.ctf.service.ChallengeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;
    private final AttemptService attemptService;
    @GetMapping("/paging")
    public ResponseEntity<List<ChallengePagingDTO>> paging(@PageableDefault(page=1)Pageable page){
        List<ChallengePagingDTO> paging = challengeService.paging(page);
        if(paging !=null){
            return ResponseEntity.ok(paging);
        }
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("/문제에대한모든시도들")
    public ResponseEntity getAttempts(){
       //challenge id 받아오기
        Long id = (long) 0.0; // 나중에 고칠거
        try{
            return new ResponseEntity(attemptService.getChallengeAttempt(id), HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
