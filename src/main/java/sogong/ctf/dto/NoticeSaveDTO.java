package sogong.ctf.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoticeSaveDTO {
    private String title;
    private String content;
}
