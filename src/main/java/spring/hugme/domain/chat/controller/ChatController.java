package spring.hugme.domain.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.hugme.domain.chat.dto.ChatBotDogListResponse;
import spring.hugme.domain.chat.dto.ChatBotResponse;
import spring.hugme.domain.chat.dto.ChatMessageListResponse;
import spring.hugme.domain.chat.dto.ChatResponse;
import spring.hugme.domain.chat.dto.ChatRoomRequest;
import spring.hugme.domain.chat.dto.ChatStartRequest;
import spring.hugme.domain.chat.model.service.ChatService;
import spring.hugme.domain.chat.model.service.DogMemoryService;
import spring.hugme.global.controller.BaseController;
import spring.hugme.global.response.CommonApiResponse;
import spring.hugme.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.API_V1, produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatController {

  private final ChatService chatService;
  private final DogMemoryService dogMemoryService;

  @PostMapping("/chat/{dogId}")
  public CommonApiResponse<ChatBotResponse> ChatRoomCreate(@PathVariable Long dogId, @RequestBody ChatRoomRequest request, @AuthenticationPrincipal String userId){

    ChatBotResponse response = chatService.ChatRoomCreate(dogId, userId, request);

    return CommonApiResponse.success(
        ResponseCode.CREATED,
        "정상적으로 채팅방 생성이 완료되었습니다.",
        response

    );
  }

  @PostMapping("/chat/send")
  public CommonApiResponse<ChatResponse> LLMChatResponse(@RequestBody ChatStartRequest chatStartRequest, @AuthenticationPrincipal String userId){

    ChatResponse response = chatService.LLMChatResponse(chatStartRequest, userId);


    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 채팅이 응답이 완료되었습니다.",
        response
    );
  }


  //채팅방 대화 기록 보기
  @GetMapping("/chat/message/{chatBotId}")
  public CommonApiResponse<List<ChatMessageListResponse>> messageList(@PathVariable Long chatBotId){

    List<ChatMessageListResponse> response = chatService.messageList(chatBotId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 채팅 기록이 응답이 완료되었습니다.",
        response
    );
  }

  //강아지 챗봇 목록 불러오기
  @GetMapping("/chat/list")
  public CommonApiResponse<List<ChatBotDogListResponse>> ChatBotDogList(@AuthenticationPrincipal String userId){

    List<ChatBotDogListResponse> response = chatService.chatBotDogList(userId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 챗봇목록이 불러져왔습니다.",
        response
    );
  }

  //챗봇 대화방 삭제하기
  @DeleteMapping("/chat/list/{chatBotId}")
  public CommonApiResponse<String> ChatBotDogDelete(@PathVariable Long chatBotId){

    chatService.chatBotDogDelete(chatBotId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 챗봇이 삭제되었습니다."
    );
  }

}
