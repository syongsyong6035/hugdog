package spring.hugme.domain.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import spring.hugme.global.code.SenderType;

@Data
@Builder
public class ChatMessageResponse {

  Long chatMessageId;

  SenderType role;

  String content;

  LocalDateTime createdAt;

}
