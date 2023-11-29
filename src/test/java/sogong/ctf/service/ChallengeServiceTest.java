package sogong.ctf.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sogong.ctf.domain.Category;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.request.ChallengeSaveDTO;
import sogong.ctf.dto.request.MemberRequestDTO;
import sogong.ctf.repository.CategoryRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ChallengeServiceTest {

    public Member member1;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private FileService fileService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CategoryRepository categoryRepository;
    private Category category;

    @BeforeEach
    void setup() {
        Long test1 = memberService.join(new MemberRequestDTO("test", "test", "test", "test", "test"));
        Long test2 = memberService.join(new MemberRequestDTO("test2", "test", "test", "test", "test"));
        member1 = memberService.findMemberById(test1);
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
        Challenge find = challengeService.findByChallengeId(save).get();
        Assertions.assertThat(save).isEqualTo(find.getId());
    }

    @Test
    @DisplayName("문제 출제_파일 있는 경우")
    void saveChallengeWithFile() throws IOException {
        //given
        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("image", "test.png", "image/png",
                new FileInputStream("C://Users//오주은//Pictures//Screenshots//스크린샷 2023-11-25 152454.png")));

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
        Challenge find = challengeService.findByChallengeId(save).get();
        int findFiles = fileService.getFiles(find).size();

        Assertions.assertThat(save).isEqualTo(find.getId());
        Assertions.assertThat(findFiles).isEqualTo(1);
    }


}