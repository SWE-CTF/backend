package sogong.ctf.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class QuestionSaveDTO {
    private String title;
    private String content;
    private long challengeId;

}
