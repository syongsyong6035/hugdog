package spring.hugme.domain.community.model.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.hugme.domain.community.entity.Favorite;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.model.repo.custom.FavoriteRepositoryCustom;
import spring.hugme.domain.user.entity.Member;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> ,
    FavoriteRepositoryCustom {

  Favorite findByMemberAndPost(Member member, Post post);

  List<Favorite> findByPost(Post post);
}
