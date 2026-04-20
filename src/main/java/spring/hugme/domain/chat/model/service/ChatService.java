package spring.hugme.domain.chat.model.service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.hugme.domain.chat.dto.ChatBotDogListResponse;
import spring.hugme.domain.chat.dto.ChatBotResponse;
import spring.hugme.domain.chat.dto.ChatMessageDto;
import spring.hugme.domain.chat.dto.ChatMessageListResponse;
import spring.hugme.domain.chat.dto.ChatMessageResponse;
import spring.hugme.domain.chat.dto.ChatResponse;
import spring.hugme.domain.chat.dto.ChatRoomRequest;
import spring.hugme.domain.chat.dto.ChatStartRequest;
import spring.hugme.domain.chat.dto.LastMessageDto;
import spring.hugme.domain.chat.entity.ChatBot;
import spring.hugme.domain.chat.entity.ChatMessage;
import spring.hugme.domain.chat.model.repo.ChatBotRepository;
import spring.hugme.domain.chat.model.repo.ChatMessageRepository;
import spring.hugme.domain.dog.model.entity.Dog;
import spring.hugme.domain.dog.model.repo.DogRepository;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.domain.user.repository.UserRepository;
import spring.hugme.global.code.SenderType;
import spring.hugme.global.error.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final DogAssistant dogBot;
  private final UserRepository userRepository;
  private final DogRepository dogRepository;
  private final ChatBotRepository chatBotRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final RedisMessageService redisMessageService;



  @Transactional
  public ChatResponse LLMChatResponse(ChatStartRequest request, String userId) {

    Member member = userRepository.findByUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 멤버가 존재하지 않습니다."));

    ChatBot chatBot = chatBotRepository.findById(request.getChatBotId())
        .orElseThrow(() -> new NotFoundException("해당 챗봇이 존재하지 않습니다."));

    Dog dog = dogRepository.findById(request.getDogId())
        .orElseThrow(() -> new NotFoundException("해당 강아지가 존재하지 않습니다"));

    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

    String RainbowTrue;

    if(dog.getIsRainbow()){
      RainbowTrue = "무지개 다리를 건너 천국에 있는";
    }else{
      RainbowTrue = "현재 살아서 주인 옆에 있는";
    }


    String assistantMessage = dogBot. chat(request.getChatBotId(), String.valueOf(dog.getDogId()),dog.getDogName(), member.getName(), chatBot.getDogFeature(), dog.getAge(), dog.getBreed(), RainbowTrue, request.getGender(), today,request.getUserMessage());

    ChatMessageDto humanDto = new ChatMessageDto(request.getChatBotId(), SenderType.HUMAN, request.getUserMessage(), LocalDateTime.now());

    ChatMessageDto machineDto = new ChatMessageDto(request.getChatBotId(), SenderType.MACHINE, assistantMessage, LocalDateTime.now());



    //일단 캐싱해두기
    redisMessageService.cacheChatMessage(request.getChatBotId(), humanDto);
    redisMessageService.cacheChatMessage(request.getChatBotId(), machineDto);

    redisMessageService.saveLastMessage(chatBot.getChatbotId(), assistantMessage, LocalDateTime.now());

    return ChatResponse.builder()
        .chatBotId(request.getChatBotId())
        .userMessage(humanDto)
        .assistantMessage(machineDto)
        .build();




  }

  @Transactional
  public ChatBotResponse ChatRoomCreate(Long dogId, String userId, ChatRoomRequest request) {

    Member member = userRepository.findByUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 멤버가 존재하지 않습니다."));

    Dog dog = dogRepository.findById(dogId)
        .orElseThrow(() -> new NotFoundException("해당 강아지가 존재하지 않습니다"));

    ChatBot chatBot = ChatBot.builder()
        .lastMessage(null)
        .member(member)
        .dog(dog)
        .dogFeature(request.getDogFeature())
        .build();

    chatBotRepository.save(chatBot);

    return ChatBotResponse.builder()
        .userId(member.getId())
        .dogId(dog.getDogId())
        .chatBotId(chatBot.getChatbotId())
        .build();
  }

  public List<ChatMessageListResponse> messageList(Long chatBotId) {
    ChatBot chatBot = chatBotRepository.findById(chatBotId)
        .orElseThrow(() -> new NotFoundException("해당 채팅방이 존재하지 않습니다"));

    // DB 데이터 조회
    List<ChatMessage> messages = chatMessageRepository.findByChatBotOrderByCreatedAtAsc(chatBot);

    // 가변 리스트로 변환 (ArrayList여야 나중에 추가가 가능해요!)
    List<ChatMessageListResponse> totalMessages = new ArrayList<>(messages.stream()
        .map(message -> ChatMessageListResponse.builder()
            .chatMessageId(message.getChatMessageId())
            .content(message.getContent())
            .isSender(message.getIsSender())
            .createdAt(message.getCreatedAt())
            .updatedAt(message.getModifiedAt())
            .build())
        .toList());

    // Redis 데이터 합치기
    return redisMessageService.newMessageTotal(chatBotId, totalMessages);
  }

  public List<ChatBotDogListResponse> chatBotDogList(String userId) {

    Member member = userRepository.findByUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 멤버가 존재하지 않습니다."));


    List<ChatBot> chatBots = chatBotRepository.findByMemberAndDog(member);


    return chatBots.stream()
        .map( chatBot -> {


              LastMessageDto dto =redisMessageService.getLastMessage(chatBot.getChatbotId());
              String lastMessage;
              String createdAt;
              if(dto == null){

                lastMessage = null;
                createdAt =null;

              }else{
                lastMessage = dto.getContent();
                createdAt = dto.getCreatedAt();
              }
               return ChatBotDogListResponse.builder()
                  .dogId(chatBot.getDog().getDogId())
                  .chatBotId(chatBot.getChatbotId())
                  .lastMessage(lastMessage)
                  .userId(chatBot.getMember().getId())
                  .dogName(chatBot.getDog().getDogName())
                  .dogImageURl(chatBot.getDog().getImageURL())
                  .createdAt(createdAt)
                   .rainbowFeature(chatBot.getDog().getIsRainbow())
                  .build();

            }

        ).toList();


  }

  @Transactional
  public void chatBotDogDelete(Long chatBotId) {

    ChatBot chatBot = chatBotRepository.findById(chatBotId)
        .orElseThrow(() -> new NotFoundException("해당 챗봇이 존재하지 않습니다"));

    chatBot.setActivated(false);

  }
}
