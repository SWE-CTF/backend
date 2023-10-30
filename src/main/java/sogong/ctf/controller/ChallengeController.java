package sogong.ctf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.CategoryListDTO;
import sogong.ctf.dto.ChallengeListDTO;
import sogong.ctf.dto.ChallengePagingDTO;
import sogong.ctf.dto.ChallengeSaveDTO;
import sogong.ctf.service.AuthUser;
import sogong.ctf.service.CategoryService;
import sogong.ctf.service.ChallengeService;
import sogong.ctf.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;
    private final CategoryService categoryService;
    private final MemberService memberService;

    @GetMapping("/paging")
    public ResponseEntity<List<ChallengePagingDTO>> paging(@PageableDefault(page = 1) Pageable page) {
        List<ChallengePagingDTO> paging = challengeService.paging(page);
        if (paging != null) {
            return ResponseEntity.ok(paging);
        } else return ResponseEntity.notFound().build();
    }

    @PostMapping("/save")
    public ResponseEntity saveChallenge(ChallengeSaveDTO saveForm, @AuthUser Member member) {
        Long save = challengeService.save(saveForm, member);
        if (save != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryListDTO>> getCategories() {
        List<CategoryListDTO> categoryList = categoryService.getCategoryList();
        return ResponseEntity.ok(categoryList);
    }

    @GetMapping("search")
    public ResponseEntity<List<ChallengeListDTO>> search(@PathVariable(name = "keyword") String keyword) {
        try {
            List<ChallengeListDTO> searchResult = challengeService.search(keyword);
            return ResponseEntity.ok(searchResult);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
