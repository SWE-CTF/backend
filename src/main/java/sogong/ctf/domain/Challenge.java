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
    private float memory;
    private float time;
    private int point;

    @ManyToMany
    @JoinTable(
            name = "Challenge_Category",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @Builder
    public Challenge(String title, String content,float memory, float time, int point){
        this.title = title;
        this.content = content;
        this.memory = memory;
        this.time = time;
        this.point = point;
    }

    public void addCategory(Category category){
        this.categories.add(category);
    }
}
