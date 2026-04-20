package spring.hugme.domain.mypage.dto;


import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyInfoResponse {

  private String userId;

  private String email;

  private String name;

  private LocalDate birthday;

  private String phone;

  private String profileURL;

}
