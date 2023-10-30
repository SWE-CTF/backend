package sogong.ctf.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Post {
    private String title;
    private String content;
    @Column(updatable = false)
    private LocalDateTime writeTime;

    public Post(String title, String content, LocalDateTime writeTime) {
        this.title = title;
        this.content = content;
        this.writeTime = writeTime;
    }
}
