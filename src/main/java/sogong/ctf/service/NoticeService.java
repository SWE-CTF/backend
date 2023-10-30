package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Notice;
import sogong.ctf.dto.NoticePagingDTO;
import sogong.ctf.dto.NoticeResponseDTO;
import sogong.ctf.dto.NoticeSaveDTO;
import sogong.ctf.repository.NoticeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public long save(Member member, NoticeSaveDTO saveForm) {

        Notice n = Notice.builder().memberId(member)
                .title(saveForm.getTitle())
                .content(saveForm.getContent())
                .writeTime(LocalDateTime.now())
                .build();

        Notice save = noticeRepository.save(n);
        return save.getId();
    }

    public Optional<Notice> findByNoticeId(long noticeId) {
        return noticeRepository.findById(noticeId);
    }

    public Member findWriter(long noticeId) {
        Member writer = findByNoticeId(noticeId).get().getMemberId();
        return writer;
    }

    public NoticeResponseDTO getDetails(long noticeId) {
        Optional<Notice> n = findByNoticeId(noticeId);
        if (n.isEmpty()) return null;
        return NoticeResponseDTO.toNoticeResponseDTO(n.get());
    }

    @Transactional
    public void delete(long noticeId) {
        Notice n = findByNoticeId(noticeId).get();
        noticeRepository.delete(n);
    }

    @Transactional
    public void update(long noticeId, NoticeSaveDTO noticeSaveDTO) {
        Notice n = findByNoticeId(noticeId).orElseThrow(() -> new NoSuchElementException());
        n.updateNotice(noticeSaveDTO.getTitle(), noticeSaveDTO.getContent());
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
        Notice notice = findByNoticeId(noticeId).get();
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
