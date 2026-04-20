package spring.hugme.domain.community.model.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.hugme.domain.community.entity.Board;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.model.repo.custom.PostRepositoryCustom;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {



}
