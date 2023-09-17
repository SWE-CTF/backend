package sogong.ctf.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.MemberFormDTO;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Role;
import sogong.ctf.repository.MemberRepository;

import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void joinTest(){

        //given
        MemberFormDTO memberFormDTO = new MemberFormDTO("Test","1234","Testname","test@gmail.com");

        //when
        Long save_id = memberService.join(memberFormDTO);

        //then
        Optional<Member> save_member = memberRepository.findById(save_id);
        assertTrue(save_member.isPresent());
        assertEquals("Test", save_member.get().getUsername());
        assertNotEquals("1234", save_member.get().getPassword());
        assertEquals("Testname", save_member.get().getName());
        assertEquals("test@gmail.com", save_member.get().getEmail());
        assertEquals(Role.ROLE_MEMBER.toString(),save_member.get().getRole().toString());

    }

    @Test (expected = IllegalStateException.class)
    public void joinValidateTest() throws Exception{
        //given
        MemberFormDTO memberFormDTO = new MemberFormDTO("Test","1234","Testname","test@gmail.com");
        MemberFormDTO memberFormDTO1 = new MemberFormDTO("Test","1111","Testname23","test123@gmail.com");

        //when
        memberService.join(memberFormDTO);
        memberService.join(memberFormDTO1);

        //then
        fail("예외 발생시 여기는 안나와야함");
    }

}