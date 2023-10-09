package sogong.ctf.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sogong.ctf.domain.Question;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class QuestionPagingDTO {
    private long questionId;
    private String title;
    private String nickname;
    private LocalDateTime writeTime;

    @Builder
    public QuestionPagingDTO(long questionId, String title, String nickname, LocalDateTime writeTime) {
        this.questionId = questionId;
        this.title = title;
        this.nickname = nickname;
        this.writeTime = writeTime;
    }

    public static QuestionPagingDTO toDTO(Question question) {

        QuestionPagingDTO dto = QuestionPagingDTO.builder()
                .questionId(question.getId())
                .title(question.getTitle())
                .nickname(question.getMemberId().getNickname())
                .writeTime(question.getWriteTime())
                .build();
        return dto;
    }

}
