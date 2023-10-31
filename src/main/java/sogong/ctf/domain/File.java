package sogong.ctf.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class File {
    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;
    private String originalFileName;
    private String storedFileName;
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;

}
