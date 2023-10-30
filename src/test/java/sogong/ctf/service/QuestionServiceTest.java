package sogong.ctf.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import sogong.ctf.domain.Challenge;
import sogong.ctf.dto.QuestionSaveDTO;
import sogong.ctf.repository.QuestionRepository;

@SpringBootTest
@WithUserDetails(value="zoouniak@naver.com",userDetailsServiceBeanName = "customMemberDetailsService")
class QuestionServiceTest {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChallengeService challengeService;

    @Test @DisplayName("글 저장")
    void save(){
        Long challengeId =1L;
        //given
        QuestionSaveDTO saveDTO = QuestionSaveDTO.builder()
                .title("글 제목")
                .content("글 내용")
                .challengeId(challengeId)
                .build();
        Challenge challenge = challengeService.findByChallengeId(challengeId).get();

        /*when
        long saveId = questionService.save(member, saveDTO, challenge);
        //then
        Question findQ = questionRepository.findById(saveId).orElseThrow(()->new EntityNotFoundException("게시글이 존재하지 않습니다"));

        assertThat(saveId).isEqualTo(findQ.getId());*/

    }

}