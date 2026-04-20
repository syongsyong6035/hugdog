package spring.hugme.domain.mypage.service;


import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.hugme.domain.chat.entity.ChatBot;
import spring.hugme.domain.chat.entity.ChatMessage;
import spring.hugme.domain.chat.model.repo.ChatBotRepository;
import spring.hugme.domain.chat.model.repo.ChatMessageRepository;
import spring.hugme.domain.dog.model.entity.Dog;
import spring.hugme.domain.dog.model.repo.DogRepository;
import spring.hugme.domain.mypage.dto.MyDogCreateRequest;
import spring.hugme.domain.mypage.dto.MyDogListResponse;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.domain.user.repository.UserRepository;
import spring.hugme.global.error.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class MypageDogService {

  private final DogRepository dogRepository;
  private final UserRepository userRepository;
  private final ChatBotRepository chatBotRepository;
  private final ChatMessageRepository chatMessageRepository;

  public List<MyDogListResponse> myDogList(String userId) {

    Member member = userRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다"));


    List<Dog> dogs = dogRepository.findByMember(member);

    return dogs.stream()
        .map(dog -> {

          Boolean chatStatus = chatBotRepository.existsByDog(dog);

          return MyDogListResponse.builder()
              .userId(member.getUserId())
              .dogId(dog.getDogId())
              .name(dog.getDogName())
              .birthday(dog.getBirth())
              .breed(dog.getBreed())
              .gender(dog.getGender())
              .isNeutered(dog.getIsNeutered())
              .dogSize(dog.getDogSize())
              .weight(dog.getWeight())
              .isRainbow(dog.getIsRainbow())
              .chatStatus(chatStatus)
              .build();
        }).toList();


  }

  @Transactional
  public Long myDogCreate(String userId, MyDogCreateRequest request) {

    Member member = userRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다"));

    String imageURL = null;
    if(request.getImageURL()!= null){
      imageURL = request.getImageURL();
    }
    Dog dog = Dog.builder()
        .member(member)
        .dogName(request.getDogName())
        .gender(request.getGender())
        .isNeutered(request.getIsNeutered())
        .breed(request.getBreed())
        .birth(request.getBirth())
        .disease(request.getDisease())
        .imageURL(imageURL)
        .dogSize(request.getDogSize())
        .weight(request.getWeight())
        .isRainbow(request.getIsRainbow())
        .build();
    dogRepository.save(dog);

    return dog.getDogId();
  }

  @Transactional
  public void myDogModify(Long dogId, MyDogCreateRequest request) {

    Dog dog = dogRepository.findById(dogId)
        .orElseThrow(() -> new NotFoundException("해당 강아지가 존재하지 않습니다"));

    dog.setDogName(request.getDogName());
    dog.setGender(request.getGender());
    dog.setIsNeutered(request.getIsNeutered());
    dog.setBreed(request.getBreed());
    dog.setBirth(request.getBirth());
    dog.setDisease(request.getDisease());
    dog.setImageURL(request.getImageURL());

    dog.setDogSize(request.getDogSize());
    dog.setWeight(request.getWeight());
    dog.setIsRainbow(request.getIsRainbow());
  }


  @Transactional
  public void myDogDelete(Long dogId) {

    Dog dog = dogRepository.findById(dogId)
        .orElseThrow(() -> new NotFoundException("해당 강아지가 존재하지 않습니다"));

    ChatBot chatBot = chatBotRepository.findByDog(dog);



    if (chatBot != null) {


      List<ChatMessage> chatMessages = chatMessageRepository.findByChatBot(chatBot)
          .orElse(null);

      if (chatMessages != null && !chatMessages.isEmpty()) {

        chatMessageRepository.deleteAllInBatch(chatMessages);
      }

      chatBotRepository.delete(chatBot);
    }

    dogRepository.delete(dog);

  }
}
