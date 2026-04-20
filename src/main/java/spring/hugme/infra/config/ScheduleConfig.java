package spring.hugme.infra.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.hugme.domain.chat.dto.ChatMessageDto;
import spring.hugme.domain.chat.entity.ChatBot;
import spring.hugme.domain.chat.entity.ChatMessage;
import spring.hugme.domain.chat.model.repo.ChatBotRepository;
import spring.hugme.domain.chat.model.repo.ChatMessageRepository;
import spring.hugme.domain.chat.model.service.DogMemoryService;
import spring.hugme.domain.user.entity.Member;

@Component
@RequiredArgsConstructor
public class ScheduleConfig {

  private final DogMemoryService dogMemoryService;
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;
  private final ChatBotRepository chatBotRepository;
  private final ChatMessageRepository chatMessageRepository;

  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
  @Async
  public void chatLogUpdate(){

    Set<Object> dirtyBotIds = redisTemplate.opsForSet().members("chat:dirty:set");

    for(Object botIdObj : dirtyBotIds){
      //저장해야하는 아이디만 가져오기 위해서
      Long chatBotId = Long.parseLong((String) botIdObj);
      String chatKey = "chat:cache:" + chatBotId;

      List<Object> cacheMessage = redisTemplate.opsForList().range(chatKey, 0, -1);
      if(cacheMessage == null || cacheMessage.isEmpty()) continue;

      ChatBot chatBot = chatBotRepository.findByDogAndChatBot(chatBotId);
      if(chatBot == null) continue;


      StringBuilder fullChatBuilder = new StringBuilder();

      //저장되어있는 캐시값들 한번에 값 저장하기
      List<ChatMessage> messages = cacheMessage.stream()
          .map(chat -> {

            ChatMessageDto dto = null;
            try {
              dto = objectMapper.readValue((String) chat, ChatMessageDto.class);
            } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
            }
            fullChatBuilder.append(dto.getRole()).append(": ").append(dto.getContent()).append("\n");

            return ChatMessage.builder()
                .content(dto.getContent())
                .isSender(dto.getRole())
                .chatBot(chatBot)
                .build();

          }).toList();

      dogMemoryService.rememberConversation(fullChatBuilder.toString(), chatBot);
      chatMessageRepository.saveAll(messages);
      redisTemplate.delete(chatKey);
      redisTemplate.opsForSet().remove("chat:dirty:set", botIdObj.toString());
    }

  }

}
