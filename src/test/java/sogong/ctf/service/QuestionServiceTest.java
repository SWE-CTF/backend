package sogong.ctf.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.config.security.CustomMemberDetails;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Question;
import sogong.ctf.domain.Role;
import sogong.ctf.dto.request.QuestionSaveDTO;
import sogong.ctf.dto.response.QuestionResponseDTO;
import sogong.ctf.exception.QuestionNotFoundException;
import sogong.ctf.mockConfig.WithCustomMockUser;
import sogong.ctf.repository.ChallengeRepository;
import sogong.ctf.repository.MemberRepository;
import sogong.ctf.repository.QuestionRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private MemberRepository memberRepository;

    private Member member1;
    private Challenge challenge;

    /*
     * 질문과 관련된 서비스를 테스트하기 위해
     * Member 객체와 Challenge 객체를 저장한다.
     */
    @BeforeEach
    void setup() {
        member1 = memberRepository.save(Member.builder().name("test").role(Role.ROLE_MEMBER).build());
        challenge = challengeRepository.save(Challenge.builder()
                .title("은주오의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .memory(128f)
                .time(6f)
                .examiner(member1)
                .fileExist(false)
                .build());
    }

    @Test
    @DisplayName("글 저장")
    void save() {
        // given
        QuestionSaveDTO saveDTO = QuestionSaveDTO.builder()
                .challengeId(challenge.getId())
                .title("저장 테스트 제목")
                .content("글 저장 테스트")
                .build();

        // when
        long save = questionService.save(member1, saveDTO, challenge);
        // then
        Question find = questionRepository.findById(save).get();
        assertThat(find.getId()).isEqualTo(save);
    }

    @Test
    @DisplayName("저장된 글을 조회하면 글이 조회되어야한다")
    void getDetailSucc() {
        // given
        Question save = questionRepository.save(Question.builder()
                .memberId(member1)
                .title("질문 조회 테스트")
                .content("내용")
                .challengeId(challenge)
                .build());

        // when
        QuestionResponseDTO details = questionService.getDetails(save.getId());
        // then
        Assertions.assertThat(details.getQuestionId()).isEqualTo(save.getId());
    }

    @Test
    @DisplayName("저장되지 않은 글을 조회하면 조회되지않아야한다.")
    void getDetailFail() {
        // given
        // 저장된 질문글이 없다

        // when
        // then
        assertThrows(QuestionNotFoundException.class, () -> questionService.getDetails(1));
    }

    @Test
    @DisplayName("작성자가 질문을 수정하면 수정되어야한다")
    void updateSucc() {
        // given
        Question save = questionRepository.save(Question.builder()
                .memberId(member1)
                .title("질문 수정 테스트")
                .content("내용")
                .challengeId(challenge)
                .build());

        QuestionSaveDTO updateDTO = QuestionSaveDTO.builder()
                .challengeId(challenge.getId())
                .title("수정되었습니다")
                .content("내용")
                .build();
        // when
        questionService.update(save.getId(), updateDTO, member1);
        // then
        Question find = questionRepository.findById(save.getId()).get();
        assertThat(find.getTitle()).isEqualTo("수정되었습니다");

    }

    @Test
    @WithCustomMockUser
    @DisplayName("작성자가 아닌 사용자가 질문을 수정하면 수정되지않아야한다")
    void updateFail() {
        // given
        Question save = questionRepository.save(Question.builder()
                .memberId(member1)
                .title("질문 조회 테스트")
                .content("내용")
                .challengeId(challenge)
                .build());

        QuestionSaveDTO updateDTO = QuestionSaveDTO.builder()
                .challengeId(challenge.getId())
                .title("수정되었습니다")
                .content("내용")
                .build();
        // 작성자가 아닌 사용자
        CustomMemberDetails principal = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member user = principal.getMember();
        ReflectionTestUtils.setField(user, "id", 2L);
        // when, then
        assertThrows(AccessDeniedException.class, () -> questionService.update(save.getId(), updateDTO, user));
    }

    @Test
    @DisplayName("작성자가 질문을 삭제하면 삭제되어야한다")
    void deleteSucc() {
        // given
        Question save = questionRepository.save(Question.builder()
                .memberId(member1)
                .title("질문 조회 테스트")
                .content("내용")
                .challengeId(challenge)
                .build());

        // when
        questionService.delete(save.getId(), member1);
        // then
        Optional<Question> find = questionRepository.findById(save.getId());
        assertThat(find).isEmpty();
    }

    @Test
    @WithCustomMockUser
    @DisplayName("작성자가 아닌 사용자가 질문을 삭제하면 삭제되지않아야한다")
    void deleteFail() {
        // given
        Question save = questionRepository.save(Question.builder()
                .memberId(member1)
                .title("질문 조회 테스트")
                .content("내용")
                .challengeId(challenge)
                .build());

        // 작성자가 아닌 사용자
        CustomMemberDetails principal = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member user = principal.getMember();
        ReflectionTestUtils.setField(user, "id", 2L);
        // when, then
        assertThrows(AccessDeniedException.class, () -> questionService.delete(save.getId(), user));
    }
}