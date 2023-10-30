package sogong.ctf.domain;

import lombok.*;

import javax.persistence.*;

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
    private String examiner;//출제자
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;

    /*@ManyToMany
    @JoinTable(
            name = "category",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();*/

    @Builder
    public Challenge(String title, String content, float memory, float time) {
        this.title = title;
        this.content = content;
        this.memory = memory;
        this.time = time;
    }

    /*public void addCategory(Category category){
        this.categories.add(category);
    }*/
    //문제 등록할 때.. 힌트!
}
