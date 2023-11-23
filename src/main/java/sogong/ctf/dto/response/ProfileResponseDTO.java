package sogong.ctf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDTO {
    private int attemptCount;
    private int correctCount;
    private int incorrectCount;
    Set<Long> correctChallengeId;
    Set<Long> failChallengeId;
    Set<Long> allChallengeId;
}
