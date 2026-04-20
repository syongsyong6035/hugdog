package spring.hugme.domain.community.model.repo.custom;

import java.util.List;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.user.entity.Member;

public interface FavoriteRepositoryCustom {


  List<Post> findLikedPostsByMember(Member member);
}
