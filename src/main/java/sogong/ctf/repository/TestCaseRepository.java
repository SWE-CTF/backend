package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.TestCase;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}
