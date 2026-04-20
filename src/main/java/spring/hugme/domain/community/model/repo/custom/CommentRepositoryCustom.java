package spring.hugme.domain.community.model.repo.custom;

import spring.hugme.domain.community.entity.Comments;
import spring.hugme.domain.community.entity.Post;
import java.util.List;
import spring.hugme.domain.user.entity.Member;

public interface CommentRepositoryCustom {

  List<Comments> findAllCommentsWithMemberByPost(Post post);
  List<Comments> findByMember(Member member);
}
