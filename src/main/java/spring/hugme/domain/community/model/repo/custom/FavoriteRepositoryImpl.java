package spring.hugme.domain.community.model.repo.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.entity.QBoard;
import spring.hugme.domain.community.entity.QFavorite;
import spring.hugme.domain.community.entity.QPost;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.domain.user.entity.QMember;

@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Post> findLikedPostsByMember(Member targetMember) {
    QPost p = QPost.post;
    QFavorite f = QFavorite.favorite;
    QMember m = QMember.member;
    QBoard b = QBoard.board;

    return queryFactory
        .select(p)
        .from(f)
        .join(f.post, p)
        .join(p.member, m).fetchJoin()
        .join(p.board, b).fetchJoin()
        .where(
            f.member.eq(targetMember),
            f.activated.isTrue()
        )
        .orderBy(f.createdAt.desc())      // 최근에 좋아요 누른 순서대로
        .fetch();
  }
}
