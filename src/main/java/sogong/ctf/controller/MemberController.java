package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sogong.ctf.dto.MemberFormDTO;
import sogong.ctf.domain.Member;
import sogong.ctf.service.MemberService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/member/join")
    public ResponseEntity<Optional<Member>> joinMember(@RequestBody MemberFormDTO memberFormDTO){
        Long member_id = memberService.join(memberFormDTO);
        Optional<Member> find_member = memberService.findOne(member_id);
        return ResponseEntity.ok(find_member);
    }

    @GetMapping("/")
    public String test(){
        return "index";
    }

}
