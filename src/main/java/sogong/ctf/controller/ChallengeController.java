package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.request.ChallengeSaveDTO;
import sogong.ctf.dto.request.ChallengeSearchDTO;
import sogong.ctf.dto.response.CategoryListDTO;
import sogong.ctf.dto.response.ChallengePagingDTO;
import sogong.ctf.dto.response.ChallengeResponseDTO;
import sogong.ctf.service.AuthUser;
import sogong.ctf.service.CategoryService;
import sogong.ctf.service.ChallengeService;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;
    private final CategoryService categoryService;

    /*
    문제 페이징
     */
    @GetMapping("/paging")
    public ResponseEntity<List<ChallengePagingDTO>> paging(@PageableDefault(page = 1) Pageable page) {
        List<ChallengePagingDTO> paging = challengeService.paging(page);
        if (paging.size() != 0) {
            return ResponseEntity.ok(paging);
        } else return ResponseEntity.notFound().build();
    }

    /*
    문제 출제 시 필요한 카테고리 번호 리스트 응답
     */
    @GetMapping("/save")
    public ResponseEntity<List<CategoryListDTO>> categoryForSave() {
        List<CategoryListDTO> categoryList = categoryService.getCategoryList();
        return ResponseEntity.ok(categoryList);
    }

    /*
    문제 출제
     */
    @PostMapping("/save")
    public ResponseEntity saveChallenge(@RequestPart("saveForm") @Valid ChallengeSaveDTO saveForm,
                                        @RequestPart(value = "files", required = false) List<MultipartFile> files, @AuthUser Member member) {
        try {
            saveForm.setFiles(files);
            challengeService.save(saveForm, member);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
    문제 상세조회
     */
    @GetMapping("{challengeId}")
    public ResponseEntity<ChallengeResponseDTO> getChallenge(@PathVariable("challengeId") int challengeId) {
        try {
            ChallengeResponseDTO details = challengeService.getDetails(challengeId);
            return ResponseEntity.ok(details);

        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
    문제 삭제
     */
    @DeleteMapping("/{challengeId}")
    public ResponseEntity deleteChallenge(@PathVariable("challengeId") int challengeId, @AuthUser Member member) {
        long examinerId = challengeService.findExaminer(challengeId);
        if (examinerId != member.getId()) {
            return ResponseEntity.status(403).build();//출제자와 삭제하려는 사용자가 다른 경우
        }
        try {
            challengeService.deleteChallenge(challengeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
    문제 수정
     */
    @PutMapping("{challengeId}")
    public ResponseEntity updateChallenge(@PathVariable("challengeId") int challengeId,
                                          @RequestPart("saveForm") ChallengeSaveDTO updateForm,
                                          @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                          @AuthUser Member member) {
        updateForm.setFiles(files);
        boolean update = challengeService.updateChallenge(challengeId, updateForm, member);
        if (update) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
   keyword를 포함하는 문제 제목 검색
    */
    @GetMapping("search")
    public ResponseEntity<List<ChallengeSearchDTO>> search(@RequestParam("keyword") String keyword) {
        try {
            List<ChallengeSearchDTO> searchResult = challengeService.search(keyword);
            return ResponseEntity.ok(searchResult);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryListDTO>> getCategories() {
        List<CategoryListDTO> categoryList = categoryService.getCategoryList();
        return ResponseEntity.ok(categoryList);
    }

}
