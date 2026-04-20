package spring.hugme.domain.chat.model.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.hugme.domain.chat.entity.ChatBot;
import spring.hugme.domain.chat.model.repo.custom.ChatBotRepositoryCustom;
import spring.hugme.domain.dog.model.entity.Dog;

@Repository
public interface ChatBotRepository extends JpaRepository<ChatBot, Long>, ChatBotRepositoryCustom {

  Boolean existsByDog(Dog dog);

  ChatBot findByDog(Dog dog);
}
