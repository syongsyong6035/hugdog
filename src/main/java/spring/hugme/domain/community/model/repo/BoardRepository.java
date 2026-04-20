package spring.hugme.domain.community.model.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.hugme.global.code.BoardAlias;
import spring.hugme.domain.community.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

  Optional<Board> findByType(BoardAlias type);
}
