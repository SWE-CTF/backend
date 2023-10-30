package sogong.ctf.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class Post {
    private String title;
    private String content;
    @Column(updatable = false)
    private LocalDateTime writeTime;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

    public Post(String title, String content, LocalDateTime writeTime, Member memberId) {
        this.title = title;
        this.content = content;
        this.writeTime = writeTime;
        this.memberId = memberId;
    }

    protected void updateTitle(String title) {
        this.title = title;
    }

    protected void updateContent(String content) {
        this.content = content;
    }
}
