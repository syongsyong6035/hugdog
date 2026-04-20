package spring.hugme.domain.chat.model.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.hugme.domain.chat.entity.ChatBot;
import spring.hugme.domain.chat.entity.ChatMessage;
import spring.hugme.domain.user.entity.Member;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {



  List<ChatMessage> findByChatBotOrderByCreatedAtAsc(ChatBot chatBot);

  Optional<List<ChatMessage>> findByChatBot(ChatBot chatBot);

}
