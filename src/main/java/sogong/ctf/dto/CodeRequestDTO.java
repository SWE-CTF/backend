package sogong.ctf.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CodeRequestDTO {
    private String language;
    private String code;
    private Long challengeID;
}
