package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.dto.ChallengeListDTO;
import sogong.ctf.dto.ChallengePagingDTO;
import sogong.ctf.dto.ChallengeSaveDTO;
import sogong.ctf.repository.ChallengeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
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
        Challenge c = Challenge.builder()
                .title(saveForm.getTitle())
                .content(saveForm.getContent())
                .time(saveForm.getTime())
                .memory(saveForm.getMemory())
                .examiner(member)
                .build();
        Challenge save = challengeRepository.save(c);
        if (saveForm.getFiles() != null) {
            challengeFileService.save(saveForm.getFiles(),save);
        }
        return save.getId();
    }

    public List<ChallengeListDTO> search(String keyword) {
        List<Challenge> searchResult = challengeRepository.findAllByTitleContaining(keyword);
        List<ChallengeListDTO> list = new ArrayList<>();
        for (Challenge challenge : searchResult) {
            list.add(ChallengeListDTO.builder().title(challenge.getTitle()).build());
        }
        return list;
    }
}
