package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.Team;
import sogong.ctf.dto.TeamFormDTO;
import sogong.ctf.repository.TeamRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public Long createTeam(TeamFormDTO teamFormDTO){

        validateDuplicateTeam(teamFormDTO);

        Team team = Team.builder()
                .name(teamFormDTO.getName())
                .build();

        return teamRepository.save(team).getId();
    }

    public List<String> findAllTeam(){
        return teamRepository.findAllByOnlyName();
    }

    private void validateDuplicateTeam(TeamFormDTO teamFormDTO){
        Optional<Team> find_team = teamRepository.findByName(teamFormDTO.getName());
        if(!find_team.isEmpty())
            throw new IllegalStateException("이미 존재하는 소속입니다.");
    }
}
