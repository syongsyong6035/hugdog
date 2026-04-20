package spring.hugme.domain.mypage.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MyInfoModifyRequest {

  private String userId;

  private String email;

  private String name;

  private LocalDate birthday;

  private String phone;

}
