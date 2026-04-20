package spring.hugme.global.code;

public enum DogGender {

  MALE("남자"),
  FEMALE("여자");

  private final String description;

  DogGender(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
