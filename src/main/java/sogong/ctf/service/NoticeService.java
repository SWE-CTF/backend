package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Notice;
import sogong.ctf.dto.request.NoticeSaveDTO;
import sogong.ctf.dto.response.NoticePagingDTO;
import sogong.ctf.dto.response.NoticeResponseDTO;
import sogong.ctf.exception.ErrorCode;
import sogong.ctf.exception.NoticeNotFoundException;
import sogong.ctf.repository.NoticeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberService memberService;

    @Transactional
    public long save(NoticeSaveDTO saveForm, Member member) {

        Notice n = Notice.builder().memberId(member)
                .title(saveForm.getTitle())
                .content(saveForm.getContent())
                .writeTime(LocalDateTime.now())
                .build();

        Notice save = noticeRepository.save(n);
        return save.getId();
    }

    public Notice findByNoticeId(long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(() -> new NoticeNotFoundException(ErrorCode.NOTICE_NOT_EXIST));
    }

    public Member findWriter(long noticeId) {
        return findByNoticeId(noticeId).getMemberId();
    }

    public NoticeResponseDTO getDetails(long noticeId) {
        Notice notice = findByNoticeId(noticeId);
        seeNotice(noticeId); // 조회수 증가

        return NoticeResponseDTO.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .writer(notice.getMemberId().getNickname())
                .writeTime(notice.getWriteTime())
                .readCnt(notice.getReadCnt())
                .build();
    }

    @Transactional
    public void delete(long noticeId, Member member) {
        Member writer = findWriter(noticeId);
        if (memberService.IsEquals(writer, member)) {
            Notice n = findByNoticeId(noticeId);
            noticeRepository.delete(n);
        } else throw new AccessDeniedException("사용자와 작성자가 일치하지 않습니다");
    }

    @Transactional
    public void update(long noticeId, NoticeSaveDTO noticeSaveDTO, Member member) {
        Member writer = findWriter(noticeId);
        if (memberService.IsEquals(writer, member)) {
            Notice n = findByNoticeId(noticeId);
            n.updateNotice(noticeSaveDTO.getTitle(), noticeSaveDTO.getContent());
        } else throw new AccessDeniedException("사용자와 작성자가 일치하지 않습니다");
    }

    public List<NoticePagingDTO> paging(Pageable page) {
        Page<Notice> notices = findAllPage(page);
        List<NoticePagingDTO> pagingList = new ArrayList<>();
        for (Notice notice : notices) {
            pagingList.add(NoticePagingDTO.toDTO(notice));
        }
        return pagingList;
    }

    @Transactional
    public void seeNotice(long noticeId) {
        Notice notice = findByNoticeId(noticeId);
        notice.increaseReadCnt();
    }

    private Page<Notice> findAllPage(Pageable page) {
        int p = page.getPageNumber() - 1;
        int pageLimit = 10;
        Page<Notice> notices = noticeRepository.findAll(PageRequest.of(p, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        return notices;
    }

    public int getTotalPage(Pageable page) {
        Page<Notice> questions = findAllPage(page);
        return questions.getTotalPages();
    }

}
