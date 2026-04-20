package spring.hugme.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum SenderType {

  HUMAN("사람"),
  MACHINE("기계");

  private final String description;

  SenderType(String description) {
    this.description = description;
  }
}