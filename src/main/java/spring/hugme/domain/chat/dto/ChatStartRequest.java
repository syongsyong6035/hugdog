package spring.hugme.domain.chat.dto;

import lombok.Data;

@Data
public class ChatStartRequest {

  //dogId
  Long dogId;

  Long chatBotId;


  //보호자 성별
  String gender;

  //보낸 메세지
  String userMessage;


}
