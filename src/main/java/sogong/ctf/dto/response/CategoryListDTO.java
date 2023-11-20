package sogong.ctf.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class CategoryListDTO {
    private Long categoryId;
    private String categoryName;
    private int cnt;

    @Builder
    public CategoryListDTO(Long categoryId, String categoryName, int cnt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.cnt = cnt;
    }
}
