package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.response.NoticePagingDTO;
import sogong.ctf.dto.response.NoticeResponseDTO;
import sogong.ctf.dto.request.NoticeSaveDTO;
import sogong.ctf.service.AuthUser;
import sogong.ctf.service.MemberService;
import sogong.ctf.service.NoticeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/notice")
public class NoticeController {
    private final NoticeService noticeService;
    private final MemberService memberService;

    @PostMapping("/save")//공지사항 작성
    public ResponseEntity saveNotice(@RequestBody NoticeSaveDTO saveForm, @AuthUser Member member) {//질문 작성
        try {
            noticeService.save(member, saveForm);//질문 저장
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{noticeId}") //공지사항 상세조회
    public ResponseEntity<NoticeResponseDTO> getNotice(@PathVariable("noticeId") long noticeId) {
        NoticeResponseDTO notice = noticeService.getDetails(noticeId);
        if (notice != null) {
            noticeService.seeNotice(noticeId);//조회수 증가
            return ResponseEntity.ok(notice);
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity deleteNotice(@PathVariable("noticeId") long noticeId, @AuthUser Member member) {
        Member writer = noticeService.findWriter(noticeId);
        if (memberService.IsEquals(member, writer)) {//글 작성자와 지우려고 하는 사람 일치 여부 확인
            noticeService.delete(noticeId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity updateNotice(@PathVariable("noticeId") long noticeId, @RequestBody NoticeSaveDTO saveForm, @AuthUser Member member) {
        Member writer = noticeService.findWriter(noticeId);
        if (memberService.IsEquals(member, writer)) {
            noticeService.update(noticeId, saveForm);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/paging")
    public ResponseEntity<Map<String, Object>> paging(@PageableDefault(page = 1) Pageable page) {
        List<NoticePagingDTO> paging = noticeService.paging(page);
        if (paging.size() == 0) {
            return ResponseEntity.status(404).build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("paging", paging);
        return ResponseEntity.ok(response);
    }

}
