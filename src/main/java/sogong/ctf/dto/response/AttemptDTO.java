package sogong.ctf.dto.response;

import lombok.*;
import sogong.ctf.domain.CodeStatus;

@RequiredArgsConstructor
@Getter
@Setter
@Data
@Builder
public class AttemptDTO {
    private String code;
    private CodeStatus codeStatus;
    private Long challengeId;

    @Builder
    public AttemptDTO(String code, CodeStatus codeStatus, Long challengeId) {
        this.code = code;
        this.codeStatus = codeStatus;
        this.challengeId = challengeId;
    }
}
