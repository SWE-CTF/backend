package sogong.ctf.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Team;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TeamRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void teamAndMemberTest(){
        //given
        Team team = Team.builder()
                .name("A TEAM")
                .build();
        teamRepository.save(team);

        Member member = Member.builder()
                .username("testID")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .build();

        member.joinTeam(team);

        memberRepository.save(member);

        //when
        Optional<Member> find_member = memberRepository.findById(member.getId());

        assertEquals("testID",find_member.get().getUsername());
        assertEquals("testPassword",find_member.get().getPassword());
        assertEquals("testName",find_member.get().getName());
        assertEquals("testEmail",find_member.get().getEmail());
        assertEquals("A TEAM",find_member.get().getTeam().getName());

        assertEquals(find_member.get(), member);

    }

}