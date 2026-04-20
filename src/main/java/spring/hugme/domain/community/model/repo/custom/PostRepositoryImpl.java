package spring.hugme.domain.community.model.repo.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import spring.hugme.domain.community.dto.PostListProjection;
import spring.hugme.domain.community.entity.Board;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.entity.PostHashtag;
import spring.hugme.domain.community.entity.QBoard;
import spring.hugme.domain.community.entity.QComments;
import spring.hugme.domain.community.entity.QFavorite;
import spring.hugme.domain.community.entity.QPost;
import spring.hugme.domain.community.entity.QPostHashtag;
import spring.hugme.domain.user.entity.Member;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Post> findAllByBoardWithBoardAndMember(Board board) {
    QBoard b = QBoard.board;
    QPost p = QPost.post;

    return queryFactory.selectFrom(p)
        .join(p.board, b).fetchJoin()
        .join(p.member).fetchJoin()
        .leftJoin(p.hashtagList).fetchJoin()
        .where(p.board.eq(board)
            .and(p.activated.eq(true)))
        .fetch();
  }

  @Override
  public List<Post> findAllWithAllRelations() {
    QPost p = QPost.post;

    return queryFactory.selectFrom(p)
        .join(p.board).fetchJoin()
        .join(p.member).fetchJoin()
        .leftJoin(p.hashtagList).fetchJoin()
        .where(p.activated.eq(true))
        .fetch();
  }

  @Override
  public Post findByPostIdWithAllRelations(Long postId) {
    QPost p = QPost.post;

    return queryFactory.selectFrom(p)
        .join(p.board).fetchJoin()
        .join(p.member).fetchJoin()
        .leftJoin(p.hashtagList).fetchJoin()
        .where(p.postId.eq(postId))
        .fetchOne();
  }


  @Override
  public PostListProjection findCountsByPostId(Long postId) {
    QPost p = QPost.post;
    QComments c = QComments.comments;
    QFavorite f = QFavorite.favorite;

    return queryFactory
        .select(
            Projections.constructor(PostListProjection.class,
                p.postId,

                queryFactory.select(c.count())
                    .from(c)
                    .where(c.post.eq(p)
                        .and(c.activated.eq(true))),

                queryFactory.select(f.count())
                    .from(f)
                    .where(f.post.eq(p)
                        .and(f.activated.eq(true)))
            )
        )
        .from(p)
        .where(p.postId.eq(postId)) // 특정 게시글만 조회
        .fetchOne();
  }

  @Override
  public List<Post> findAllByRecommendPost(Long postId, List<PostHashtag> hashtagList, int i) {
    QPost post = QPost.post;
    QPostHashtag postHashtag = QPostHashtag.postHashtag;


    List<String> tagNames = hashtagList.stream()
        .map(ph -> ph.getHashtagContent())
        .toList();

    // 태그가 하나도 없으면 추천할 게 없으니 빈 리스트 반환
    if (tagNames.isEmpty()) {
      return Collections.emptyList();
    }

    return queryFactory
        .selectFrom(post)

        .join(post.hashtagList, postHashtag)
        .where(
            postHashtag.hashtagContent.in(tagNames),
            post.postId.ne(postId),
            post.activated.isTrue()
        )
        .groupBy(post.postId)
        .orderBy(
            postHashtag.count().desc(), // 1순위: 겹치는 태그 개수가 많은 순
            post.createdAt.desc()       // 2순위: 최신순
        )
        .limit(i)
        .fetch();
  }

  @Override
  public List<Post> findAllMemberWithAllRelations(Member member) {
     QPost p = QPost.post;

     return queryFactory.selectFrom(p)
          .join(p.board).fetchJoin()
          .leftJoin(p.hashtagList).fetchJoin()
          .where(p.member.eq(member)
              .and(p.activated.eq(true)))
          .fetch();
    }

}

