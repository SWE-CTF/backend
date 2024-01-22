package sogong.ctf.dto.response;

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
    long questionId;
    long challengeId;
    String title;
    String content;
    String writer;
    long commentId;
    LocalDateTime writeTime;
    List<CommentResponseDTO> commentList;

    @Builder
    public QuestionResponseDTO(long questionId, long challengeId, String title, String content, String writer, long commentId, LocalDateTime writeTime) {
        this.questionId = questionId;
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.commentId = commentId;
        this.writeTime = writeTime;
        this.commentList = new ArrayList<>();
    }

    public static QuestionResponseDTO toQuestionResponseDTO(Question q) {
        long commentId = (q.getAdoptedComment() != null) ? q.getAdoptedComment().getId() : -1;
        return QuestionResponseDTO.builder()
                .questionId(q.getId())
                .challengeId(q.getChallenge().getId())
                .title(q.getTitle())
                .content(q.getContent())
                .writer(q.getMember().getNickname())
                .commentId(commentId)
                .writeTime(q.getWriteTime())
                .build();
    }
}
