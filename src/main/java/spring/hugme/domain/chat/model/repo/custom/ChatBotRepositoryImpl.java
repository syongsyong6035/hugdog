package spring.hugme.domain.chat.model.repo.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import spring.hugme.domain.chat.entity.ChatBot;
import spring.hugme.domain.chat.entity.QChatBot;
import spring.hugme.domain.dog.model.entity.QDog;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.domain.user.entity.QMember;

@RequiredArgsConstructor
public class ChatBotRepositoryImpl implements ChatBotRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  @Override
  public List<ChatBot> findByMemberAndDog(Member member) {
    QMember m = QMember.member;
    QChatBot c = QChatBot.chatBot;
    QDog d = QDog.dog;

    return queryFactory.selectFrom(c)
        .join(c.member, m).fetchJoin()
        .join(c.dog, d).fetchJoin()
        .where(c.member.eq(member)
            .and(c.activated.eq(true)))
        .fetch();

  }

  @Override
  public ChatBot findByDogAndChatBot(Long chatBot) {
    QMember m = QMember.member;
    QChatBot c = QChatBot.chatBot;
    QDog d = QDog.dog;

    return queryFactory.selectFrom(c)
        .join(c.member, m).fetchJoin()
        .join(c.dog, d).fetchJoin()
        .where(c.chatbotId.eq(chatBot)
            .and(c.activated.eq(true)))
        .fetchOne();
  }
}
