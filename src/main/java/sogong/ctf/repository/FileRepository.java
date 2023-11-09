package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sogong.ctf.domain.File;

public interface FileRepository extends JpaRepository<File,Long> {
}
