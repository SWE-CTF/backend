package sogong.ctf.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CodeResponseDTO {
    String language;
    String code;
    float memory;
    float time;
}
