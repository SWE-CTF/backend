package sogong.ctf.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sogong.ctf.domain.Notice;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
public class NoticeResponseDTO {
    String title;
    String content;
    String writer;
    LocalDateTime writeTime;
    int like;
    int dislike;

    @Builder
    public NoticeResponseDTO(long challengeId, String title, String content, String writer, long commentId, LocalDateTime writeTime) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.writeTime = writeTime;
    }

    public static NoticeResponseDTO toNoticeResponseDTO(Notice n) {
        return NoticeResponseDTO.builder()
                .title(n.getTitle())
                .content(n.getContent())
                .writer(n.getMemberId().getNickname())
                .writeTime(n.getWriteTime())
                .build();
    }
}
