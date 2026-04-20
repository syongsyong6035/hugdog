package spring.hugme.domain.user.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.domain.user.entity.QMember;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Member> findUserId(String userId) {

    QMember m = QMember.member;

    return Optional.ofNullable(queryFactory.selectFrom(m)
        .where(m.userId.eq(userId)
            .and(m.activated.eq(true)))
        .fetchOne());
  }
}
