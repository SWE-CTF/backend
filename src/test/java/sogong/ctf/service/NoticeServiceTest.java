package sogong.ctf.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.config.security.CustomMemberDetails;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Notice;
import sogong.ctf.dto.request.NoticeSaveDTO;
import sogong.ctf.dto.response.NoticeResponseDTO;
import sogong.ctf.mockConfig.WithCustomMockAdmin;
import sogong.ctf.repository.NoticeRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@WithCustomMockAdmin
class NoticeServiceTest {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private NoticeRepository noticeRepository;
    private Member admin;

    @BeforeEach
    public void setup() {
        CustomMemberDetails principal = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        admin = principal.getMember();
        ReflectionTestUtils.setField(admin, "id", 2L);
    }

    @Test
    @DisplayName("관리자는 공지사항을 작성할 수 있다")
    void saveSucc() {
        // given
        NoticeSaveDTO saveDTO = new NoticeSaveDTO("공지사항 작성", "테스트해보기");
        // when
        long save = noticeService.save(saveDTO, admin);
        // then
        Notice find = noticeRepository.findById(save).get();
        assertEquals(save, find.getId());
    }

    @Test
    @DisplayName("사용자는 공지사항을 확인할 수 있다")
    void getDetails() {
        // given
        Notice save = noticeRepository.save(Notice.builder()
                .title("공지사항")
                .content("테스트")
                .memberId(admin)
                .writeTime(LocalDateTime.now())
                .build()
        );
        // when
        NoticeResponseDTO details = noticeService.getDetails(save.getId());
        // then
        assertEquals("공지사항", details.getTitle());
    }

    @Test
    @DisplayName("사용자가 공지사항을 확인하면 조회수가 증가한다")
    void getDetailsResultIncreaseReadCnt() {
        // given
        Notice save = noticeRepository.save(Notice.builder()
                .title("공지사항")
                .content("테스트")
                .memberId(admin)
                .writeTime(LocalDateTime.now())
                .build()
        );
        // when
        noticeService.getDetails(save.getId());
        // then
        Notice find = noticeRepository.findById(save.getId()).get();
        assertEquals(1, find.getReadCnt());
    }

    @Test
    @DisplayName("작성자는 공지사항을 삭제할 수 있다")
    void delete() {
        // given
        Notice save = noticeRepository.save(Notice.builder()
                .title("공지사항")
                .content("테스트")
                .memberId(admin)
                .writeTime(LocalDateTime.now())
                .build()
        );
        // when
        noticeService.delete(save.getId(), admin);
        // then
        Optional<Notice> find = noticeRepository.findById(save.getId());
        assertThat(find).isEmpty();
    }

    @Test
    @DisplayName("작성자는 공지사항을 수정할 수 있다")
    void update() {
        // given
        Notice save = noticeRepository.save(Notice.builder()
                .title("공지사항")
                .content("테스트")
                .memberId(admin)
                .writeTime(LocalDateTime.now())
                .build()
        );
        NoticeSaveDTO updateForm = new NoticeSaveDTO("수정", "테스트");
        // when
        noticeService.update(save.getId(), updateForm, admin);
        // then
        Notice find = noticeRepository.findById(save.getId()).get();
        Assertions.assertEquals(updateForm.getTitle(), find.getTitle());
    }
}