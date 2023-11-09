package sogong.ctf.dto;

import lombok.*;
import sogong.ctf.domain.Comment;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CommentResponseDTO {
    private long commentId;
    private String nickname;
    private String content;
    private LocalDateTime writeTime;

    public static CommentResponseDTO toCommentResponseDTO(Comment comment) {
       return CommentResponseDTO.builder()
               .commentId(comment.getId())
               .nickname(comment.getWriter().getNickname())
               .content(comment.getContent())
               .writeTime(comment.getWriteTime())
               .build();
    }

}
