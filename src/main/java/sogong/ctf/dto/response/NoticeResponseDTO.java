package sogong.ctf.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
public class NoticeResponseDTO {
    String title;
    String content;
    String writer;
    LocalDateTime writeTime;
    int readCnt;

    @Builder
    public NoticeResponseDTO(String title, String content, String writer, LocalDateTime writeTime, int readCnt) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.writeTime = writeTime;
        this.readCnt = readCnt;
    }
}
