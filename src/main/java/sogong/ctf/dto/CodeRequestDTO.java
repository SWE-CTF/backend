package sogong.ctf.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CodeRequestDTO {
    String language;
    String code;
    Long challengeID;
}
