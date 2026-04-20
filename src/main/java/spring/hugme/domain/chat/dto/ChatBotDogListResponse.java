package spring.hugme.domain.chat.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatBotDogListResponse {

  Long dogId;

  Long chatBotId;

  String lastMessage;

  UUID userId;

  String dogName;

  String dogImageURl;

  String createdAt;

  Boolean rainbowFeature;
}
