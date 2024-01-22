package sogong.ctf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sogong.ctf.domain.Challenge;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChallengePagingDTO {
    private long id;
    private String title;
    private String examiner;
    private int correct_cnt;

    public static ChallengePagingDTO toChallengePagingDTO(Challenge challenge) {
        ChallengePagingDTO dto = new ChallengePagingDTO();
        dto.setId(challenge.getId());
        dto.setTitle(challenge.getTitle());
        dto.setExaminer(challenge.getMember().getNickname());
        dto.setCorrect_cnt(challenge.getCorrectCnt());
        return dto;
    }
}
