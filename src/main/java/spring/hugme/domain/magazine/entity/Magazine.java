package spring.hugme.domain.magazine.entity;

import io.swagger.v3.core.util.Json;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Magazine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int magazineId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resource_id")
  private Resource resource;

  private String cancerTitle;

  //타입을 파싱할건지 아니면 jsonnode로 할건지에 따라 바꾸는게 필요
  private String contentOrigin;

  private String contentTranslation;

  private String contentSummary;

  private String resourceName;

  private String resourceURL;


}
