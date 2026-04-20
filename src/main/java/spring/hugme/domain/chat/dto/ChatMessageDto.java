package spring.hugme.domain.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import spring.hugme.global.code.SenderType;

@Data
@Builder
public class ChatMessageDto {

  Long chatBotId;

  SenderType role;

  String content;

  LocalDateTime createdAt;


  public ChatMessageDto(Long chatBotId, SenderType role, String content, LocalDateTime createdAt){
    this.chatBotId = chatBotId;
    this.role = role;
    this.content = content;
    this.createdAt = createdAt;

  }


}
