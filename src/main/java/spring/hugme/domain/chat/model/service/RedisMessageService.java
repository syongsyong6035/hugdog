package spring.hugme.domain.chat.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import spring.hugme.domain.chat.dto.ChatMessageDto;
import spring.hugme.domain.chat.dto.ChatMessageListResponse;
import spring.hugme.domain.chat.dto.ChatMessageResponse;
import spring.hugme.domain.chat.dto.LastMessageDto;

@Service
@RequiredArgsConstructor
public class RedisMessageService {

  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  public void saveLastMessage(Long chatBotId, String content, LocalDateTime createdAt){
    String key = "chatroom:" + chatBotId +":last";
    LastMessageDto dto = new LastMessageDto(content, createdAt.toString());

    redisTemplate.opsForValue().set(key, dto);
  }

  public LastMessageDto getLastMessage(Long chatBotId){
    String key = "chatroom:" + chatBotId +":last";

    return (LastMessageDto) redisTemplate.opsForValue().get(key);
  }

  public void cacheChatMessage(Long chatBotId, ChatMessageDto messaageDto){

    String key = "chat:cache:" + chatBotId;

    try{

      String jsonChatMemory = objectMapper.writeValueAsString(messaageDto);
      redisTemplate.opsForList().rightPush(key, jsonChatMemory);
      redisTemplate.expire(key, 1, TimeUnit.HOURS);

      redisTemplate.opsForSet().add("chat:dirty:set", String.valueOf(chatBotId));

    }catch (JsonProcessingException e){

    }

  }


  public List<ChatMessageListResponse> newMessageTotal(Long chatBotId, List<ChatMessageListResponse> totalMessages) {
    String chatKey = "chat:cache:" + chatBotId;
    List<Object> cachedData = redisTemplate.opsForList().range(chatKey, 0, -1);

    if (cachedData != null && !cachedData.isEmpty()) {
      cachedData.forEach(json -> {
        try {
          ChatMessageDto dto = objectMapper.readValue((String) json, ChatMessageDto.class);
          totalMessages.add(ChatMessageListResponse.builder()
              .chatMessageId(null)
              .content(dto.getContent())
              .isSender(dto.getRole())
              .createdAt(dto.getCreatedAt())
              .updatedAt(dto.getCreatedAt())
              .build());
        } catch (JsonProcessingException e) {

        }
      });
    }
    return totalMessages;
  }

}
