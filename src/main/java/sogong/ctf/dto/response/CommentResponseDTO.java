package sogong.ctf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

}
