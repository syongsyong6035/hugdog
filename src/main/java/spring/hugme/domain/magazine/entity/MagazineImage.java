package spring.hugme.domain.magazine.entity;

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
public class MagazineImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int imageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "magazine_id")
  private Magazine magazine;

  private String originFileName;

  private String renameFileName;

  private String savePath;

  private String type;

}
