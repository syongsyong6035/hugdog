package spring.hugme.domain.chat.model.repo.custom;

import java.util.List;
import spring.hugme.domain.chat.entity.ChatBot;
import spring.hugme.domain.user.entity.Member;

public interface ChatBotRepositoryCustom {

  List<ChatBot> findByMemberAndDog(Member member);

  ChatBot findByDogAndChatBot(Long ChatBotId);
}
