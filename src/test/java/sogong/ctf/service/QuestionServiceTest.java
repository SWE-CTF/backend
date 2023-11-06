package sogong.ctf.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Question;
import sogong.ctf.dto.MemberRequestDTO;
import sogong.ctf.dto.QuestionSaveDTO;
import sogong.ctf.repository.QuestionRepository;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class QuestionServiceTest {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private MemberService memberService;
    Member member;

    @BeforeEach
    void setup(){
        Long id = memberService.join(new MemberRequestDTO("test", "test", "test", "test", "test"));
        member = memberService.findMemberById(id);

    }
    @AfterEach
    void clear(){
        questionRepository.deleteAll();
    }
    @Test @DisplayName("글 저장")
    void save(){
        //given
        Challenge challenge = new Challenge("question","question",1.0f,1.0f);
        QuestionSaveDTO saveDTO = QuestionSaveDTO.builder()
                .challengeId(1)
                .title("저장 테스트 제목")
                .content("글 저장 테스트")
                .build();

        //when
        long save = questionService.save(member, saveDTO, challenge);
        //then
        Question q = questionRepository.findById(save).orElseThrow(()->new EntityNotFoundException("게시글이 존재하지 않습니다"));
        assertThat(save).isEqualTo(q.getId());
    }

}