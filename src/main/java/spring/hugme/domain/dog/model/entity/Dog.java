package spring.hugme.domain.dog.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.global.code.DogGender;
import spring.hugme.global.code.DogSize;
import spring.hugme.infra.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Dog extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dogId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Member member;


  private String dogName;

  @Enumerated(EnumType.STRING)
  private DogGender gender;

  private Boolean isNeutered;

  private String breed;

  private LocalDate birth;

  private Boolean disease;

  private String imageURL;

  @Enumerated(EnumType.STRING)
  private DogSize dogSize;

  private float weight;

  private Boolean isRainbow;


  public int getAge() {
    if (this.birth == null) {
      return 0;
    }

    LocalDate birthDate = this.birth;
    LocalDate currentDate = LocalDate.now();

    return Period.between(birthDate, currentDate).getYears();
  }
}
