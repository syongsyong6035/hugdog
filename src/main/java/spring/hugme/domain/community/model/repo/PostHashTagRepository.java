package spring.hugme.domain.community.model.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.entity.PostHashtag;

@Repository
public interface PostHashTagRepository extends JpaRepository<PostHashtag, Long> {

  List<PostHashtag> findAllByPost(Post post);


  void deleteAllByPost(Post post);

}
