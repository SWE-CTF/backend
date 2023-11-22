package sogong.ctf.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Data
public class CodeRequestDTO {
    private String language;
    private String code;
    private Long challengeId;
}
