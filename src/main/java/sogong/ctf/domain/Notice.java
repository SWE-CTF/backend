package sogong.ctf.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends Post {
    @Id
    @GeneratedValue
    @Column(name = "notice_id")
    private long id;

    private int readCnt;


    @Builder
    public Notice(String title, String content, Member memberId, LocalDateTime writeTime) {
        super(title, content, writeTime, memberId);
        this.readCnt = 0;
    }

    public void updateNotice(String title, String content) {
        updateTitle(title);
        updateContent(content);
    }

    public void increaseReadCnt() {
        this.readCnt++;
    }

}
