package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sogong.ctf.MemberFormDTO;
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

    @PostMapping("/api/member/login")
    public ResponseEntity<Optional<Member>> loginMember(@RequestBody MemberFormDTO memberFormDTO){
        Long member_id = memberService.login(memberFormDTO);
        Optional<Member> find_member = memberService.findOne(member_id);
        return ResponseEntity.ok(find_member);
    }
}
