package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sogong.ctf.domain.Category;
import sogong.ctf.domain.Challenge;
import sogong.ctf.dto.response.CategoryListDTO;
import sogong.ctf.repository.CategoryRepository;
import sogong.ctf.repository.ChallengeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ChallengeRepository challengeRepository;

    public List<CategoryListDTO> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryListDTO> categories = new ArrayList<>();
        for (Category category : categoryList) {
            List<Challenge> allByCategoryId = challengeRepository.findAllByCategoryId(category);
            CategoryListDTO dto = CategoryListDTO.builder()
                    .categoryId(category.getId())
                    .categoryName(category.getName())
                    .cnt(allByCategoryId.size())
                    .build();
            categories.add(dto);
        }
        return categories;
    }

}
