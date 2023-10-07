package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.config.security.JwtProvider;
import sogong.ctf.dto.MemberRequestDTO;
import sogong.ctf.dto.MemberResponseDTO;
import sogong.ctf.dto.TeamFormDTO;
import sogong.ctf.dto.TokenDTO;
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
    private final JwtProvider jwtProvider;


    @GetMapping("/api/member/join") //소속 가입할때 가능한 소속 보여주기 , url 미정
    public List<String> joinForm(){
        return teamService.findAllTeam();
    }

    @PostMapping("/api/admin/team") //url 미정 , 소속 추가 기능
    public ResponseEntity<String> create(@RequestBody TeamFormDTO teamFormDTO){
        try{
            teamService.createTeam(teamFormDTO);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/api/member/login")
    public ResponseEntity<MemberResponseDTO> login(@RequestBody  MemberRequestDTO request){
        try{
            return new ResponseEntity<>(memberService.login(request),HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/member/join")
    public ResponseEntity<Long> join(@RequestBody MemberRequestDTO request){
        try{
            return new ResponseEntity<>(memberService.join(request), HttpStatus.OK);
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/member/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest httpServletRequest){
        try{
            return new ResponseEntity<>(memberService.logout(httpServletRequest),HttpStatus.OK);
        } catch(ValidationException e){
            return ResponseEntity.status(401).build();
        } catch(NoSuchProviderException e){
            return ResponseEntity.status(400).build();
        } catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

}
