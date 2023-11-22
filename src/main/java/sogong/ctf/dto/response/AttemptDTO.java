package sogong.ctf.dto.response;

import lombok.*;
import sogong.ctf.domain.CodeStatus;

@RequiredArgsConstructor
@Getter
@Setter
@Data
@Builder
public class AttemptDTO {
    private Long attemptId;
    private String code;
    private CodeStatus codeStatus;
    private Long challengeId;

    @Builder
    public AttemptDTO(Long attemptId, String code, CodeStatus codeStatus, Long challengeId) {
        this.attemptId = attemptId;
        this.code = code;
        this.codeStatus = codeStatus;
        this.challengeId = challengeId;
    }
}
