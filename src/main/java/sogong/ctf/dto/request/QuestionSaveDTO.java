package sogong.ctf.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class QuestionSaveDTO {
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private long challengeId;

    @Builder
    public QuestionSaveDTO(String title, String content, long challengeId) {
        this.title = title;
        this.content = content;
        this.challengeId = challengeId;
    }
}
