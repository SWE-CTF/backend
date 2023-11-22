package sogong.ctf.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.repository.MemberRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AttemptServiceTest {

    @Autowired
    AttemptService attemptService;

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void CompileTest(){
        //given

        //when

        //then
    }

}