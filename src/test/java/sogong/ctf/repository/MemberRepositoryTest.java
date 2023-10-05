package sogong.ctf.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Member.MemberBuilder;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void successTest(){

        //given
        Member member = Member.builder()
                .username("testID")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .build();

        //when
        Long save_id = memberRepository.save(member).getId();
        Optional<Member> find_member = memberRepository.findById(save_id);

        //then
        Member test_memeber = find_member.orElse(null);
        assertNotNull(test_memeber);
        assertEquals("testID",test_memeber.getUsername());
        assertEquals("testPassword",test_memeber.getPassword());
        assertEquals("testName",test_memeber.getName());
        assertEquals("testEmail",test_memeber.getEmail());

    }
}