package sogong.ctf.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChallengeSearchDTO {
    private long challengeId;
    private String title;
}
