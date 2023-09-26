package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
