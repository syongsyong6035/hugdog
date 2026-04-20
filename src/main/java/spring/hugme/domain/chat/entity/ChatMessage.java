package spring.hugme.domain.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.global.code.SenderType;
import spring.hugme.infra.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long chatMessageId;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chatbot_id")
  private ChatBot chatBot;

  @Lob // 방법 B: TEXT 타입으로 지정 (추천)
  @Column(columnDefinition = "TEXT")
  private String content;

  private SenderType isSender;
}
