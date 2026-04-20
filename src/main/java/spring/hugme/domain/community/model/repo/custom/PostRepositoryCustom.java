package spring.hugme.domain.community.model.repo.custom;

import java.util.List;
import spring.hugme.domain.community.dto.PostListProjection;
import spring.hugme.domain.community.entity.Board;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.entity.PostHashtag;
import spring.hugme.domain.user.entity.Member;

public interface PostRepositoryCustom {

  List<Post> findAllByBoardWithBoardAndMember(Board board);

  List<Post> findAllWithAllRelations();

  Post findByPostIdWithAllRelations(Long postId);

  PostListProjection findCountsByPostId(Long postId);

  List<Post> findAllByRecommendPost(Long postId, List<PostHashtag> hashtagList, int i);

  List<Post> findAllMemberWithAllRelations(Member member);

;
}
