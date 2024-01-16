package sogong.ctf.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sogong.ctf.config.security.CustomMemberDetails;
import sogong.ctf.domain.Category;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Role;
import sogong.ctf.dto.request.ChallengeSaveDTO;
import sogong.ctf.dto.request.ChallengeSearchDTO;
import sogong.ctf.mockConfig.WithCustomMockUser;
import sogong.ctf.repository.CategoryRepository;
import sogong.ctf.repository.ChallengeRepository;
import sogong.ctf.repository.MemberRepository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ChallengeServiceTest {
    public Member member1;
    private Category category;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private FileService fileService;


    @BeforeEach
    void setup() {
        member1 = memberRepository.save(Member.builder().name("test").role(Role.ROLE_MEMBER).build());
        category = categoryRepository.save(Category.builder().name("연산").build());
    }

    @Test
    @DisplayName("문제 출제_파일 없는 경우")
    void saveChallengeWithoutFiles() {
        //given
        ChallengeSaveDTO saveForm = ChallengeSaveDTO.builder()
                .title("은주오의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .categoryId(category.getId())
                .files(null)
                .input1("1 2")
                .output1("3")
                .input2("222 333")
                .output2("555")
                .input3("-1 -2")
                .output3("-3")
                .memory(128f)
                .time(6f)
                .build();

        //when
        long save = challengeService.save(saveForm, member1);

        //then
        Challenge find =challengeRepository.findById(save).get();
        Assertions.assertThat(save).isEqualTo(find.getId());
    }

    @Test
    @DisplayName("문제 출제_파일 있는 경우")
    void saveChallengeWithFile() {
        //given
        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("image", "test.png", "image/png", "test file".getBytes(StandardCharsets.UTF_8)));

        ChallengeSaveDTO saveForm = ChallengeSaveDTO.builder()
                .title("은주오의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .categoryId(category.getId())
                .files(files)
                .input1("1 2")
                .output1("3")
                .input2("222 333")
                .output2("555")
                .input3("-1 -2")
                .output3("-3")
                .memory(128f)
                .time(6f)
                .build();
        //when
        long save = challengeService.save(saveForm, member1);

        //then
        Challenge find = challengeRepository.findById(save).get();
        int findFiles = fileService.getFiles(find).size();

        Assertions.assertThat(save).isEqualTo(find.getId());
        Assertions.assertThat(findFiles).isEqualTo(1);
    }

    @Test
    @DisplayName("출제자가 문제를 수정하면 수정되어야한다")
    void updateByExaminer() {
        // given
        Challenge challenge = Challenge.builder()
                .title("은주오의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .categoryId(category)
                .memory(128f)
                .time(6f)
                .examiner(member1)
                .fileExist(false)
                .build();

        Challenge save = challengeRepository.save(challenge);

        ChallengeSaveDTO updateForm = ChallengeSaveDTO.builder()
                .title("오주은의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .categoryId(category.getId())
                .files(null)
                .input1("1 2")
                .output1("3")
                .input2("222 333")
                .output2("555")
                .input3("-1 -2")
                .output3("-3")
                .memory(128f)
                .time(6f)
                .build();
        // when
        challengeService.updateChallenge(save.getId(), updateForm, member1);
        // then
        Challenge find = challengeRepository.findById(save.getId()).get();
        Assertions.assertThat(find.getTitle()).isEqualTo("오주은의 덧셈 계산기");
    }

    @Test
    @WithCustomMockUser
    @DisplayName("출제자가 아닌 사용자가 수정하면 수정되지 않아야한다")
    void updateByOther() {
        Challenge challenge = Challenge.builder()
                .title("은주오의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .categoryId(category)
                .memory(128f)
                .time(6f)
                .examiner(member1)
                .fileExist(false)
                .build();

        Challenge save = challengeRepository.save(challenge);

        // 출제자가 아닌 사용자
        CustomMemberDetails principal = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member user = principal.getMember();
        ReflectionTestUtils.setField(user, "id", 2L);

        ChallengeSaveDTO updateForm = ChallengeSaveDTO.builder()
                .title("오주은의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .categoryId(category.getId())
                .files(null)
                .input1("1 2")
                .output1("3")
                .input2("222 333")
                .output2("555")
                .input3("-1 -2")
                .output3("-3")
                .memory(128f)
                .time(6f)
                .build();
        // when, then
        assertThrows(AccessDeniedException.class, () -> challengeService.updateChallenge(save.getId(), updateForm, user));

    }

    @Test
    @DisplayName("출제자가 문제를 삭제하면 삭제되어야한다")
    void deleteByExaminer() {
        // given
        Challenge challenge = Challenge.builder()
                .title("은주오의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .categoryId(category)
                .memory(128f)
                .time(6f)
                .examiner(member1)
                .fileExist(false)
                .build();

        Challenge save = challengeRepository.save(challenge);

        //when
        challengeService.deleteChallenge(save.getId(), member1);
        //then
        Optional<Challenge> find = challengeRepository.findById(save.getId());
        Assertions.assertThat(find).isEmpty();
    }

    @Test
    @WithCustomMockUser
    @DisplayName("출제자가 아닌 사용자가 삭제하면 삭제되지 않아야한다")
    void deleteByOther() {
        // given
        Challenge challenge = Challenge.builder().title("은주오의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .categoryId(category)
                .memory(128f)
                .time(6f)
                .examiner(member1)
                .fileExist(false)
                .build();

        Challenge save = challengeRepository.save(challenge);
        // 출제자가 아닌 다른 사용자
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member user = customMemberDetails.getMember();
        ReflectionTestUtils.setField(user, "id", 2L);

        // when, then
        assertThrows(AccessDeniedException.class, () -> challengeService.deleteChallenge(save.getId(), user));

    }

    @Test
    @DisplayName("제목에 keyword가 포함된 문제가 검색되어야 한다")
    void search() {
        // given
        Challenge challenge = Challenge.builder()
                .title("은주오의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .categoryId(category)
                .memory(128f)
                .time(6f)
                .examiner(member1)
                .fileExist(false)
                .build();

        challengeRepository.save(challenge);

        // when
        List<ChallengeSearchDTO> find = challengeService.search("은주오");

        // then
        Assertions.assertThat(find.size()).isEqualTo(1);

    }

}