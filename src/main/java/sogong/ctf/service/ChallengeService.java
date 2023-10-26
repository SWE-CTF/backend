package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sogong.ctf.domain.Challenge;
import sogong.ctf.dto.ChallengePagingDTO;
import sogong.ctf.repository.ChallengeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    public List<ChallengePagingDTO> paging(Pageable pageable) {
        int page= pageable.getPageNumber()-1;
        int pageLimit=10;
        Page<Challenge> challenge = challengeRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        List<ChallengePagingDTO> list = new ArrayList<>();
        for (Challenge c : challenge) {
            list.add(ChallengePagingDTO.toChallengePagingDTO(c));
        }
        return list;
    }
    public Optional<Challenge> findByChallengeId(long id){
        return challengeRepository.findById(id);

    }
}
