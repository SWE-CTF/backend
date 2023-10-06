package sogong.ctf.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {

    @Id @GeneratedValue
    @Column(name = "challenge_id")
    private Long id;

    private String title;
    private String content;
    private int correctCnt;
    private float memory;
    private float time;
    private String examiner;//출제자

    /*
    @ManyToMany
    @JoinTable(
            name = "Challenge_Category",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )


    private List<Category> categories = new ArrayList<>();
     */

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;

    @Builder
    public Challenge(String title, String content,int correctCnt,float memory, float time, String examiner){
        this.title = title;
        this.content = content;
        this.correctCnt = correctCnt;
        this.memory = memory;
        this.time = time;
        this.examiner = examiner;
    }

    /*
    public void addCategory(Category category){
        this.categories.add(category);
    }

     */
}
