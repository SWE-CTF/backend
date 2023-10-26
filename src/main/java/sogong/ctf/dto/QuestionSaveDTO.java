package sogong.ctf.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public class QuestionSaveDTO {
    private String title;
    private String content;
    private long challengeId;

    @Builder
    public QuestionSaveDTO(String title, String content, long challengeId) {
        this.title = title;
        this.content = content;
        this.challengeId = challengeId;
    }
}
