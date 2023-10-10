package sogong.ctf.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sogong.ctf.domain.Question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class QuestionResponseDTO {
    long challengeId;
    String title;
    String content;
    String writer;
    Boolean isAdopted;
    LocalDateTime writeTime;
    List<CommentResponseDTO> commentList;

    @Builder
    public QuestionResponseDTO(long challengeId, String title, String content, String writer, Boolean isAdopted, LocalDateTime writeTime) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.isAdopted = isAdopted;
        this.writeTime = writeTime;
        this.commentList = new ArrayList<>();
    }

    public static QuestionResponseDTO toQuestionResponseDTO(Question q) {
        return QuestionResponseDTO.builder()
                .challengeId(q.getChallengeId().getId())
                .title(q.getTitle())
                .content(q.getContent())
                .writer(q.getMemberId().getNickname())
                .isAdopted(q.getIsAdopted())
                .writeTime(q.getWriteTime())
                .build();
    }
}
