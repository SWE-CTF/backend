package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.MemberPasswordDTO;
import sogong.ctf.dto.MemberRequestDTO;
import sogong.ctf.dto.MemberResponseDTO;
import sogong.ctf.dto.TeamFormDTO;
import sogong.ctf.service.AuthUser;
import sogong.ctf.service.MemberService;
import sogong.ctf.service.TeamService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TeamService teamService;

    @GetMapping("/api/member/join") //소속 가입할때 가능한 소속 보여주기 , url 미정
    public ResponseEntity joinForm(){
        return new ResponseEntity(teamService.findAllTeam(), HttpStatus.OK);
    }

    @PostMapping("/api/admin/team") //url 미정 , 소속 추가 기능
    public ResponseEntity create(@RequestBody TeamFormDTO teamFormDTO){
        try{
            return new ResponseEntity(teamService.createTeam(teamFormDTO),HttpStatus.OK);
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/member/login")
    public ResponseEntity login(@RequestBody MemberRequestDTO request){
        try{
            return new ResponseEntity(memberService.login(request),HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/member/join")
    public ResponseEntity join(@RequestBody MemberRequestDTO request){
        try{
            return new ResponseEntity<>(memberService.join(request), HttpStatus.OK);
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
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/api/member/rank")
    public ResponseEntity showRank(){
        try{
            return new ResponseEntity(memberService.rank(),HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/api/member/profile")
    public ResponseEntity getProfile(@AuthUser Member member){
        try{
            return new ResponseEntity(memberService.showProfile(member),HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/api/member/profile")
    public ResponseEntity postProfile(@RequestBody MemberResponseDTO memberResponseDTO, @AuthUser Member member){
        try{
            return new ResponseEntity(memberService.postProfile(memberResponseDTO, member),HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/api/member/profile/password")
    public ResponseEntity checkPassword(@RequestBody MemberPasswordDTO memberPasswordDTO, @AuthUser Member member){
        try{
            return new ResponseEntity(memberService.checkPassword(memberPasswordDTO,member),HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/api/member/profile/password")
    public ResponseEntity updatePassword(@RequestBody MemberPasswordDTO memberPasswordDTO, @AuthUser Member member){
        try{
            return new ResponseEntity(memberService.updatePassword(memberPasswordDTO, member),HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

}
