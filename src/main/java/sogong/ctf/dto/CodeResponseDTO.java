package sogong.ctf.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CodeResponseDTO {
    private String language;
    private String code;
    private float memory;
    private float time;
}
