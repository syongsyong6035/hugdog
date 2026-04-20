package spring.hugme.domain.mypage.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import spring.hugme.global.code.DogGender;
import spring.hugme.global.code.DogSize;

@Data
@Builder
public class MyDogListResponse {

  private String userId;

  private Long dogId;

  private String name;

  private LocalDate birthday;

  private String breed;

  private DogGender gender;

  private Boolean isNeutered;

  private DogSize dogSize;

  private float weight;

  private Boolean isRainbow;

  private Boolean chatStatus;


}
