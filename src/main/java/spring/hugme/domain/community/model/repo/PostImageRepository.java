package spring.hugme.domain.community.model.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.entity.PostImage;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

  void deleteAllByPost(Post post);


  Optional<PostImage> findFirstByPostAndActivatedTrue(Post post);

  List<PostImage> findAllByPost(Post post);
}
