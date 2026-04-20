package spring.hugme.domain.chat.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatBotResponse {

  UUID userId;

  Long dogId;

  Long chatBotId;

}
