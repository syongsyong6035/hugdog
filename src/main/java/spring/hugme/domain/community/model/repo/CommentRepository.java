package spring.hugme.domain.community.model.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.hugme.domain.community.entity.Comments;

import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.model.repo.custom.CommentRepositoryCustom;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long>, CommentRepositoryCustom {

  List<Comments> findByPost(Post post);
}
