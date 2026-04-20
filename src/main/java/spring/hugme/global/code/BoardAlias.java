package spring.hugme.global.code;

import java.util.Arrays;

public enum BoardAlias {

  INFO_SHARE("info-share", "정보 공유", "특정 치료법, 영양제, 병원 후기, 재활 방법 등 객관적인 정보를 공유합니다."),
  QNA("qna", "궁금해요 (Q&A)", "보호자들이 궁금한 점을 질문하고 다른 보호자들이 경험을 공유하는 공간입니다."),
  REVIEW("review", "병원/제품 후기", "특정 동물병원, 의료기기, 항암 보조제 등에 대한 솔직한 이용 후기를 공유합니다."),

  EMERGENCY_NOTE("emergency-note", "응급 노트", "갑작스러운 응급 상황 발생 시 대처법 및 기록을 공유합니다."),
  TREATMENT_DIARY("treatment-diary", "치료/투병 일지", "정기적인 치료 진행 과정과 강아지 컨디션 변화를 기록하고 공유합니다."),

  HAPPY_MOMENT("happy-moment", "소소한 행복 기록장", "투병 중에도 포기할 수 없는 강아지와의 행복한 순간들을 기록합니다."),
  SUPPORT("support", "마음 나누기 (위로/공감)", "투병 과정에서 느끼는 힘든 감정을 솔직하게 나누고 위로와 공감을 얻는 공간입니다."),
  FREE_TALK("free-talk", "자유 게시판", "암과 직접적인 관련이 없더라도 일상적인 이야기를 나눕니다."); // 자유 게시판 추가 가정

  private final String slug;
  private final String koreanName;
  private final String description;

  BoardAlias(String slug, String koreanName, String description) {
    this.slug = slug;
    this.koreanName = koreanName;
    this.description = description;
  }

  public String getSlug() {
    return slug;
  }

  public String getKoreanName() {
    return koreanName;
  }

  public String getDescription() {
    return description;
  }

  /**
   * Slug 값(String)을 받아 해당하는 BoardType Enum 객체를 반환하는 팩토리 메서드
   */
  public static BoardAlias fromSlug(String slug) {
    return Arrays.stream(BoardAlias.values())
        .filter(type -> type.slug.equalsIgnoreCase(slug))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시판 슬러그입니다: " + slug));
  }
}
