package spring.hugme.domain.chat.model.service;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;

import dev.langchain4j.store.embedding.EmbeddingStore;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.hugme.domain.chat.dto.ChatMessageDto;
import spring.hugme.domain.chat.entity.ChatBot;


@Service
@RequiredArgsConstructor
public class DogMemoryService {

  private final EmbeddingModel embeddingModel;      // 텍스트 -> 숫자 변환
  private final EmbeddingStore<TextSegment> embeddingStore; // 벡터 DB (저장소)
  private final ChatLanguageModel chatLanguageModel; // 요약할 때 쓸 AI 모델

  /**
   * 1. 단순 텍스트(일기, 특징)를 바로 기억시키기
   */
  public void rememberFact(String text) {
    TextSegment segment = TextSegment.from(text);
    Embedding embedding = embeddingModel.embed(segment).content();
    embeddingStore.add(embedding, segment);
    System.out.println("기억 저장 완료: " + text);
  }

  /**
   * 대화 요약버전
   */
  public void rememberConversation(String fullConversation, ChatBot chatBot) {


    // 2) AI에게 요약 시키기
    String prompt = "다음 대화 내용에서 '사용자에 대한 정보'나 '강아지와의 추억'과 관련된 핵심 내용만 3줄로 요약해줘 그리고 각 시간도 담아줘:\n" + fullConversation;
    String summary = chatLanguageModel.chat(prompt);


    Metadata metaData = new Metadata();
    metaData.put("dogId", String.valueOf(chatBot.getDog().getDogId()));

    // 3) 요약된 내용을 벡터 DB에 저장 (메타데이터로 날짜 등 추가 가능)
    String today = LocalDate.now().toString();
    TextSegment segment = TextSegment.from("[" + today + "] " + "대화 기억: " + summary, metaData);
    Embedding embedding = embeddingModel.embed(segment).content();
    embeddingStore.add(embedding, segment);

    System.out.println("대화 요약 저장 완료: " + summary);
  }

}
