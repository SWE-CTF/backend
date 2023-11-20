package sogong.ctf.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class QuestionCommentSaveDTO {
    private String content;
}
