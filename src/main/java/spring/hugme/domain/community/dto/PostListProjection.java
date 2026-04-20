package spring.hugme.domain.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostListProjection {

  private Long postId;
  private Long commentCount;
  private Long likeCount;


  public PostListProjection(Long postId, Long commentCount, Long likeCount) {
    this.postId = postId;
    this.commentCount = commentCount;
    this.likeCount = likeCount;
  }

}
