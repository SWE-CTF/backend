package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.*;
import sogong.ctf.service.AuthUser;
import sogong.ctf.service.MemberService;

import javax.xml.bind.ValidationException;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/admin1") //삭제 예정
    public ResponseEntity joinAdmin(@RequestBody MemberRequestDTO request){
        try{
            System.out.println("hh");
            return ResponseEntity.ok(memberService.joinAdmin(request));
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/member/login")
    public ResponseEntity login(@RequestBody MemberRequestDTO request){
        try{
            return ResponseEntity.ok(memberService.login(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/member/join")
    public ResponseEntity join(@RequestBody MemberRequestDTO request){
        try{
            return new ResponseEntity(memberService.join(request), HttpStatus.OK);
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/member/logout")
    public ResponseEntity logout(@AuthUser Member member){
        try{
            return ResponseEntity.ok(memberService.logout(member));
        } catch(ValidationException e){
            return ResponseEntity.status(401).build();
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/api/member/rank")
    public ResponseEntity showRank(){
        try{
            return ResponseEntity.ok(memberService.rank());
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/api/member/profile")
    public ResponseEntity getProfile(@AuthUser Member member){
        try{
            return ResponseEntity.ok(memberService.showProfile(member));
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/api/member/profile")
    public ResponseEntity postProfile(@RequestBody ProfilePostDTO profilePostDTO, @AuthUser Member member){
        try{
            return ResponseEntity.ok(memberService.postProfile(profilePostDTO,member));
        }catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }
}
