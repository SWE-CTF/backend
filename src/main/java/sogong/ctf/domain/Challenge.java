package sogong.ctf.domain;

import lombok.*;
import sogong.ctf.dto.request.ChallengeSaveDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;
    private String title;
    private String content;
    private int correctCnt;
    private float memory;
    private float time;
    private String hint;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;//출제자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categoryId;
    private boolean fileExist;

    @OneToMany(mappedBy = "challengeId")
    private List<Attempt> attempts = new ArrayList<>();

    @OneToMany(mappedBy = "challengeId")
    private List<TestCase> testcases = new ArrayList<>();

    @Builder
    public Challenge(String title, String content, Category categoryId, float memory, float time, Member member, String hint, boolean fileExist) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.memory = memory;
        this.time = time;
        this.member = member;
        this.hint = hint;
        this.fileExist = fileExist;
    }

    public Challenge(Long id, String title, String content, Category categoryId, float memory, float time, Member member, String hint, boolean fileExist) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.memory = memory;
        this.time = time;
        this.member = member;
        this.hint = hint;
        this.fileExist = fileExist;
    }

    public void addAttempt(Attempt attempt) {
        this.attempts.add(attempt);
    }

    public void addTestCase(TestCase testCase) {
        this.testcases.add(testCase);
    }

    public void changeFileExist(boolean fileExist) {
        this.fileExist = fileExist;
    }

    public void addCorrectCnt() {
        this.correctCnt++;
    }

    public void update(ChallengeSaveDTO updateForm) {
        this.title = updateForm.getTitle();
        this.content = updateForm.getContent();
        this.hint = updateForm.getHint();
        this.memory = updateForm.getMemory();
        this.time = updateForm.getTime();
    }
}
