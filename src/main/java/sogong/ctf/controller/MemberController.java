package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.dto.MemberRequestDTO;
import sogong.ctf.dto.MemberResponseDTO;
import sogong.ctf.dto.TeamFormDTO;
import sogong.ctf.dto.TokenDTO;
import sogong.ctf.service.MemberService;
import sogong.ctf.service.TeamService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TeamService teamService;

    @GetMapping("/api/member/join")
    public List<String> joinForm(){
        return teamService.findAllTeam();
    }

    @PostMapping("/api/admin/team") //url 아직 안 정했습니다. 예시용
    public ResponseEntity<String> create(@RequestBody TeamFormDTO teamFormDTO){
        try{
            teamService.createTeam(teamFormDTO);
            return ResponseEntity.ok("성공");
        } catch(IllegalStateException e){
            String error_message = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error_message);
        }
    }

    @PostMapping("/api/member/login")
    public ResponseEntity<MemberResponseDTO> login(@RequestBody MemberRequestDTO request) throws Exception {
        return new ResponseEntity<>(memberService.login(request),HttpStatus.OK);
    }

    @PostMapping("/api/member/join")
    public ResponseEntity<Long> join(@RequestBody MemberRequestDTO request) throws Exception {
        return new ResponseEntity<>(memberService.join(request), HttpStatus.OK);
    }

    @GetMapping("/api/member/logout")
    public ResponseEntity<Boolean> logout(@RequestBody TokenDTO tokenDTO) throws Exception{
        return new ResponseEntity<>(memberService.logout(tokenDTO),HttpStatus.OK);
    }


    @GetMapping("/")
    public String test(){
        return "index";
    }

}
