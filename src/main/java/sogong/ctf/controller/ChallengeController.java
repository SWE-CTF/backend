package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sogong.ctf.dto.ChallengePagingDTO;
import sogong.ctf.service.ChallengeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;
    @GetMapping("/paging")
    public ResponseEntity<List<ChallengePagingDTO>> paging(@PageableDefault(page=1)Pageable page){
        List<ChallengePagingDTO> paging = challengeService.paging(page);
        if(paging !=null){
            return ResponseEntity.ok(paging);
        }
        else return ResponseEntity.notFound().build();

    }
}
