package spring.hugme.global.code;

public enum DogSize {

  SMALL("소형견", "10kg 미만"),
  MEDIUM("중형견", "10kg ~ 25kg"),
  LARGE("대형견", "25kg 이상");

  private final String title;       // 화면에 보여질 이름 (소형견)
  private final String description;

  DogSize(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }
}
