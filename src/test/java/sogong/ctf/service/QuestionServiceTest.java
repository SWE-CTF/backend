package sogong.ctf.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Question;
import sogong.ctf.dto.request.MemberRequestDTO;
import sogong.ctf.dto.response.QuestionResponseDTO;
import sogong.ctf.dto.request.QuestionSaveDTO;
import sogong.ctf.repository.ChallengeRepository;
import sogong.ctf.repository.QuestionRepository;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class QuestionServiceTest {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired

    private MemberService memberService;

    private Member member1;
    private Member member2;
    private Challenge challenge;

    @BeforeEach
    void setup() {
        Long test1 = memberService.join(new MemberRequestDTO("test", "test", "test", "test", "test"));
        Long test2 = memberService.join(new MemberRequestDTO("test2", "test", "test", "test", "test"));
        member1 = memberService.findMemberById(test1);
        member2 = memberService.findMemberById(test2);
        challenge = new Challenge("question", "question",null, 1.0f, 1.0f, member1,null,false);
        challengeRepository.save(challenge);
    }
    @Test
    @DisplayName("글 저장")
    void save() {
        //given
        QuestionSaveDTO saveDTO = QuestionSaveDTO.builder()
                .challengeId(1)
                .title("저장 테스트 제목")
                .content("글 저장 테스트")
                .build();

        //when
        long save = questionService.save(member1, saveDTO, challenge);
        //then
        Question q = questionRepository.findById(save).orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다"));
        assertThat(save).isEqualTo(q.getId());
    }

    @Test
    @DisplayName("글 조회 성공")
    void getDetailSucc() {
        //given
        QuestionSaveDTO saveDTO = QuestionSaveDTO.builder()
                .challengeId(1)
                .title("저장 테스트 제목")
                .content("글 저장 테스트")
                .build();
        long save = questionService.save(member1, saveDTO, challenge);
        //when
        QuestionResponseDTO details = questionService.getDetails(save);
        //then
        Assertions.assertThat(details.getQuestionId()).isEqualTo(save);
    }

    @Test
    @DisplayName("글 조회 실패")
    void getDeatailFail() {
        //given
        //저장된거 x
        //when
        QuestionResponseDTO details = questionService.getDetails(1);
        //then
        org.junit.jupiter.api.Assertions.assertNull(details);

    }

    @Test
    @DisplayName("글 삭제 성공")
    void deleteSucc(){
        //given
        QuestionSaveDTO saveDTO = QuestionSaveDTO.builder()
                .challengeId(1)
                .title("저장 테스트 제목")
                .content("글 저장 테스트")
                .build();
        long save = questionService.save(member1, saveDTO, challenge);
        //when
        boolean delete = questionService.delete(save, member1);
        //then
        Assertions.assertThat(delete).isTrue();
    }
    @Test
    @DisplayName("글 삭제 실패")
    void deleteFail(){
        //given
        QuestionSaveDTO saveDTO = QuestionSaveDTO.builder()
                .challengeId(1)
                .title("저장 테스트 제목")
                .content("글 저장 테스트")
                .build();
        long save = questionService.save(member1, saveDTO, challenge);
        //when
        boolean delete = questionService.delete(save, member2);
        //then
        Assertions.assertThat(delete).isFalse();
    }

    @Test
    @DisplayName("글 수정 성공")
    void updateSucc() {
        //given
        QuestionSaveDTO saveDTO = QuestionSaveDTO.builder()
                .challengeId(1)
                .title("저장 테스트 제목")
                .content("글 저장 테스트")
                .build();
        long save = questionService.save(member1, saveDTO, challenge);
        QuestionSaveDTO updateDTO = QuestionSaveDTO.builder()
                .challengeId(1)
                .title("수정 테스트 제목")
                .content("글 수정 테스트")
                .build();
        //when
        boolean update = questionService.update(save, updateDTO, member1);
        //then
        Assertions.assertThat(update).isTrue();
    }

    @Test
    @DisplayName("글 수정 실패")
    void updateFail() {
        //given
        QuestionSaveDTO saveDTO = QuestionSaveDTO.builder()
                .challengeId(1)
                .title("저장 테스트 제목")
                .content("글 저장 테스트")
                .build();
        long save = questionService.save(member1, saveDTO, challenge);
        QuestionSaveDTO updateDTO = QuestionSaveDTO.builder()
                .challengeId(1)
                .title("수정 테스트 제목")
                .content("글 수정 테스트")
                .build();
        //when
        boolean update = questionService.update(save, updateDTO, member2);
        //then
        Assertions.assertThat(update).isFalse();
    }

}