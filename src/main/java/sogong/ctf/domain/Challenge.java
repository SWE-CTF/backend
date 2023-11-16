package sogong.ctf.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue
    @Column(name = "challenge_id")
    private Long id;
    private String title;
    private String content;
    private int correctCnt;
    private float memory;
    private float time;
    private String hint;
    @ManyToOne
    @JoinColumn(name = "examiner")
    private Member examiner;//출제자

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;
    private boolean fileExist;

    @OneToMany(mappedBy = "challengeId")
    private List<Attempt> attempts = new ArrayList<>();

    @OneToMany(mappedBy = "challengeId")
    private List<TestCase> testcases = new ArrayList<>();

    @Builder
    public Challenge(String title, String content,Category categoryId, float memory, float time, Member examiner, String hint,boolean fileExist) {
        this.title = title;
        this.content = content;
        this.categoryId=categoryId;
        this.memory = memory;
        this.time = time;
        this.examiner = examiner;
        this.hint = hint;
        this.fileExist=fileExist;
    }

    public void addAttempt(Attempt attempt) {
        this.attempts.add(attempt);
    }

    public void addTestCase(TestCase testCase) {
        this.testcases.add(testCase);
    }
    public void changeFileExist(boolean fileExist){
        this.fileExist=fileExist;
    }
}
