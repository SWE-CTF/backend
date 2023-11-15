package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sogong.ctf.domain.Category;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.TestCase;
import sogong.ctf.dto.*;
import sogong.ctf.repository.CategoryRepository;
import sogong.ctf.repository.ChallengeRepository;
import sogong.ctf.repository.TestCaseRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final TestCaseRepository testCaseRepository;
    private final CategoryRepository categoryRepository;
    private final FileService challengeFileService;

    public List<ChallengePagingDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;
        Page<Challenge> challenge = challengeRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        List<ChallengePagingDTO> list = new ArrayList<>();
        for (Challenge c : challenge) {
            list.add(ChallengePagingDTO.toChallengePagingDTO(c));
        }
        return list;
    }

    public Optional<Challenge> findByChallengeId(long id) {
        return challengeRepository.findById(id);
    }

    public long save(ChallengeSaveDTO saveForm, Member member) {
        Category category = categoryRepository.findById(saveForm.getCategoryId()).get();
        Challenge c = Challenge.builder()
                .title(saveForm.getTitle())
                .content(saveForm.getContent())
                .categoryId(category)
                .time(saveForm.getTime())
                .memory(saveForm.getMemory())
                .examiner(member)
                .build();
        Challenge save = challengeRepository.save(c);
        testcase(saveForm, save);

        if (saveForm.getFiles() != null) {
            challengeFileService.save(saveForm.getFiles(), save);
        }
        return save.getId();
    }

    private void testcase(ChallengeSaveDTO saveForm, Challenge save) {
        TestCase case1 = TestCase.builder().input(saveForm.getInput1())
                .output(saveForm.getOutput1())
                .challengeId(save)
                .build();
        TestCase case2 = TestCase.builder().input(saveForm.getInput1())
                .output(saveForm.getOutput1())
                .challengeId(save)
                .build();

        TestCase case3 = TestCase.builder().input(saveForm.getInput1())
                .output(saveForm.getOutput1())
                .challengeId(save)
                .build();
        testCaseRepository.save(case1);
        testCaseRepository.save(case2);
        testCaseRepository.save(case3);

        save.addTestCase(case1);
        save.addTestCase(case2);
        save.addTestCase(case3);
    }

    public List<ChallengeSearchDTO> search(String keyword) {
        List<Challenge> searchResult = challengeRepository.findAllByTitleContaining(keyword);
        List<ChallengeSearchDTO> list = new ArrayList<>();
        for (Challenge challenge : searchResult) {
            list.add(ChallengeSearchDTO.builder().title(challenge.getTitle()).build());
        }
        return list;
    }

    public ChallengeResponseDTO getDetails(long challengeId) {
        Optional<Challenge> findChallenge = challengeRepository.findById(challengeId);//문제 찾기
        if (findChallenge.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            List<byte[]> files = challengeFileService.getFiles(findChallenge.get());//파일 찾기
            List<TestCaseDTO> testCases = findTestCases(findChallenge.get());//테스트케이스 찾기
            return ChallengeResponseDTO.toDTO(findChallenge.get(),testCases,files);
        }
    }

    private List<TestCaseDTO> findTestCases(Challenge findChallenge) {
        List<TestCase> testcases = testCaseRepository.findAllByChallengeId(findChallenge);
        List<TestCaseDTO> caseDTOS = new ArrayList<>();
        for (TestCase testcase : testcases) {
            TestCaseDTO dto = new TestCaseDTO(testcase.getInput(),testcase.getOutput());
            caseDTOS.add(dto);
        }
        return caseDTOS;
    }

    public void deleteChallenge(long challengeId) {
        Challenge challenge = findByChallengeId(challengeId).get();
        challengeRepository.delete(challenge);
    }

    public long findExaminer(long challengeId) {
        Challenge challenge = findByChallengeId(challengeId).get();
        return challenge.getExaminer().getId();
    }
}
