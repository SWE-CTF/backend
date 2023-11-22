package sogong.ctf.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sogong.ctf.domain.Notice;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class NoticePagingDTO {
    private long noticeId;
    private String title;
    private String nickname;
    private LocalDateTime writeTime;
    private int readCnt;

    @Builder
    public NoticePagingDTO(long noticeId, String title, String nickname, LocalDateTime writeTime, int readCnt) {
        this.noticeId = noticeId;
        this.title = title;
        this.nickname = nickname;
        this.writeTime = writeTime;
        this.readCnt = readCnt;
    }

    public static NoticePagingDTO toDTO(Notice notice) {
        NoticePagingDTO dto = NoticePagingDTO.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .nickname(notice.getMemberId().getNickname())
                .writeTime(notice.getWriteTime())
                .readCnt(notice.getReadCnt())
                .build();
        return dto;
    }

}
