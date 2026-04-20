package spring.hugme.domain.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import spring.hugme.global.code.SenderType;

@Data
@Builder
public class ChatMessageListResponse {

  Long chatMessageId;

  SenderType isSender;

  String content;

  LocalDateTime createdAt;

  LocalDateTime updatedAt;



}
