package spring.hugme.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatResponse {

  Long chatBotId;

  ChatMessageDto userMessage;

  ChatMessageDto assistantMessage;

}
