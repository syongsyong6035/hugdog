package spring.hugme.domain.mypage.dto;

import java.time.LocalDate;
import lombok.Data;
import spring.hugme.global.code.DogGender;
import spring.hugme.global.code.DogSize;

@Data
public class MyDogCreateRequest {
  private String dogName;
  private DogGender gender;
  private Boolean isNeutered;
  private String breed;
  private LocalDate birth;
  private Boolean disease;
  private String imageURL;
  private DogSize dogSize;
  private float weight;
  private Boolean isRainbow;
}
