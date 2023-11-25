package sogong.ctf.dto.request;

import lombok.*;

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
