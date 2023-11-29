package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.Category;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.TestCase;
import sogong.ctf.dto.request.ChallengeSaveDTO;
import sogong.ctf.dto.request.ChallengeSearchDTO;
import sogong.ctf.dto.response.ChallengePagingDTO;
import sogong.ctf.dto.response.ChallengeResponseDTO;
import sogong.ctf.dto.response.TestCaseDTO;
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

    @Transactional
    public long save(ChallengeSaveDTO saveForm, Member member) {
        Category category = categoryRepository.findById(saveForm.getCategoryId()).get();
        Challenge c = Challenge.builder()
                .title(saveForm.getTitle())
                .content(saveForm.getContent())
                .categoryId(category)
                .time(saveForm.getTime())
                .memory(saveForm.getMemory())
                .examiner(member)
                .fileExist(!(saveForm.getFiles()==null))
                .build();
        Challenge savedChallenge = challengeRepository.save(c);
        saveTestCase(saveForm, savedChallenge);

        if (saveForm.getFiles() != null) {
            challengeFileService.save(saveForm.getFiles(), savedChallenge);
        }
        return savedChallenge.getId();
    }

    private void saveTestCase(ChallengeSaveDTO saveForm, Challenge challenge) {
        TestCase case1 = TestCase.builder().input(saveForm.getInput1())
                .output(saveForm.getOutput1())
                .challengeId(challenge)
                .build();
        TestCase case2 = TestCase.builder().input(saveForm.getInput1())
                .output(saveForm.getOutput1())
                .challengeId(challenge)
                .build();

        TestCase case3 = TestCase.builder().input(saveForm.getInput1())
                .output(saveForm.getOutput1())
                .challengeId(challenge)
                .build();
        testCaseRepository.save(case1);
        testCaseRepository.save(case2);
        testCaseRepository.save(case3);

        challenge.addTestCase(case1);
        challenge.addTestCase(case2);
        challenge.addTestCase(case3);
    }

    public List<ChallengeSearchDTO> search(String keyword) {
        List<Challenge> searchResult = challengeRepository.findAllByTitleContaining(keyword);
        List<ChallengeSearchDTO> list = new ArrayList<>();
        for (Challenge challenge : searchResult) {
            list.add(ChallengeSearchDTO.builder()
                    .challengeId(challenge.getId())
                    .title(challenge.getTitle()).build());
        }
        return list;
    }

    public ChallengeResponseDTO getDetails(long challengeId) {
        Optional<Challenge> findChallenge = challengeRepository.findById(challengeId); //문제 찾기
        if (findChallenge.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            List<byte[]> files = challengeFileService.getFiles(findChallenge.get()); //파일 찾기
            List<TestCaseDTO> testCases = findTestCases(findChallenge.get()); //테스트케이스 찾기
            return ChallengeResponseDTO.toDTO(findChallenge.get(), testCases, files);
        }
    }

    /*
    테스트케이스 검색
     */
    private List<TestCaseDTO> findTestCases(Challenge findChallenge) {
        List<TestCase> testcases = testCaseRepository.findAllByChallengeId(findChallenge);
        List<TestCaseDTO> caseDTOS = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            TestCaseDTO dto = new TestCaseDTO(testcases.get(i).getInput(), testcases.get(i).getOutput());
            caseDTOS.add(dto);
        }
        return caseDTOS;
    }

    @Transactional
    public boolean deleteChallenge(long challengeId, Member member) {
        long examiner = findExaminer(challengeId); //출제자
        if (examiner == member.getId()) { //사용자와 출제자가 일치할 경우만 삭제
            Challenge challenge = findByChallengeId(challengeId).get();
            challengeRepository.delete(challenge);
            return true;
        } else
            return false;

    }

    private long findExaminer(long challengeId) {
        Challenge challenge = findByChallengeId(challengeId).get();
        return challenge.getExaminer().getId();
    }

    @Transactional
    public boolean updateChallenge(long challengeId, ChallengeSaveDTO updateForm, Member member) {
        long examiner = findExaminer(challengeId);
        if (examiner == member.getId()) { //출제자와 사용자가 일치할 경우만 수정
            Challenge challenge = findByChallengeId(challengeId).get();
            challenge.update(updateForm);

            if (challenge.isFileExist()) {
                challengeFileService.deleteFile(challenge); //기존 파일 모두 삭제
                if (updateForm.getFiles() != null) {
                    challengeFileService.save(updateForm.getFiles(), challenge); //수정된 파일 저장
                    challenge.changeFileExist(true);
                } else challenge.changeFileExist(false); //수정 후 파일 없을 경우
            }

            testCaseRepository.deleteAllByChallengeId(challenge); //기존 테스트 케이스 모두 삭제
            saveTestCase(updateForm, challenge);

            return true;
        } else //출제자가 아닌 사용자가 삭제하려는 경우
            return false;
    }
}
