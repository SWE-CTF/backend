package sogong.ctf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sogong.ctf.domain.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team,Long> {

    Optional<Team> findByName(String name);

    @Query("SELECT t.name FROM Team t")
    List<String> findAllOnlyName();
}
