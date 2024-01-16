package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.request.NoticeSaveDTO;
import sogong.ctf.dto.response.NoticePagingDTO;
import sogong.ctf.dto.response.NoticeResponseDTO;
import sogong.ctf.service.AuthUser;
import sogong.ctf.service.NoticeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notice")
public class NoticeController {
    private final NoticeService noticeService;

    /* *
     * 공지사항 작성
     */
    @PostMapping("/save")
    public ResponseEntity saveNotice(@RequestBody @Valid NoticeSaveDTO saveForm, @AuthUser Member member) {
        noticeService.save(saveForm, member);
        return ResponseEntity.ok().build();
    }

    /* *
     * 공지사항 상세조회
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDTO> getNotice(@PathVariable("noticeId") long noticeId) {
        NoticeResponseDTO notice = noticeService.getDetails(noticeId);
        return ResponseEntity.ok(notice);
    }

    /* *
     * 공지사항 식제
     */
    @DeleteMapping("/{noticeId}")
    public ResponseEntity deleteNotice(@PathVariable("noticeId") long noticeId, @AuthUser Member member) {
        noticeService.delete(noticeId, member);
        return ResponseEntity.ok().build();
    }

    /* *
     * 공지사항 수정
     */
    @PutMapping("/{noticeId}")
    public ResponseEntity updateNotice(@PathVariable("noticeId") long noticeId, @RequestBody @Valid NoticeSaveDTO updateForm, @AuthUser Member member) {
        noticeService.update(noticeId, updateForm, member);
        return ResponseEntity.ok().build();
    }

    /* *
     * 공지사항 페이징
     */
    @GetMapping("/paging")
    public ResponseEntity<List<NoticePagingDTO>> paging(@PageableDefault(page = 1) Pageable page) {
        List<NoticePagingDTO> paging = noticeService.paging(page);
        return ResponseEntity.ok(paging);
    }

}
