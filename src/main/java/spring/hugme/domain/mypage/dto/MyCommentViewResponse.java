package spring.hugme.domain.mypage.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MyCommentViewResponse {

  private Long postId;

  private String title;

  private String comment;

  private Long commentCount;

  private Long LikeCount;

  private LocalDateTime postCreatedAt;

  private LocalDateTime commentCreatedAt;

  private LocalDateTime postModifyAt;

  private LocalDateTime commentModifyAt;


}
