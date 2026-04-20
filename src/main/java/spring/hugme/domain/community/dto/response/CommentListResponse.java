package spring.hugme.domain.community.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentListResponse {

  Long commentId;

  String userId;

  String name;

  String content;

  LocalDateTime createdAt;

  LocalDateTime updatedAt;

  String profileImageUrl;

}
