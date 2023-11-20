package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.dto.request.MemberRequestDTO;
import sogong.ctf.dto.response.MemberResponseDTO;
import sogong.ctf.dto.response.TeamFormDTO;
import sogong.ctf.service.MemberService;
import sogong.ctf.service.TeamService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.security.NoSuchProviderException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TeamService teamService;

    @GetMapping("/api/member/join") //소속 가입할때 가능한 소속 보여주기 , url 미정
    public ResponseEntity<List<String>> joinForm(){
        try {
            return ResponseEntity.ok(teamService.findAllTeam());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/admin/team") //url 미정 , 소속 추가 기능
    public ResponseEntity create(@RequestBody TeamFormDTO teamFormDTO){
        try{
            teamService.createTeam(teamFormDTO);
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/member/login")
    public ResponseEntity login(@RequestBody MemberRequestDTO request){
        try{
            MemberResponseDTO login = memberService.login(request);
            return ResponseEntity.ok(login);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/member/join")
    public ResponseEntity join(@RequestBody MemberRequestDTO request){
        try{
            memberService.join(request);
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/member/logout")
    public ResponseEntity logout(HttpServletRequest httpServletRequest){
        try{
            return new ResponseEntity(memberService.logout(httpServletRequest),HttpStatus.OK);
        } catch(ValidationException e){
            return ResponseEntity.status(401).build();
        } catch(NoSuchProviderException e){
            return ResponseEntity.status(400).build();
        } catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }
}
