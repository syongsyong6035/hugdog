package spring.hugme.domain.community.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentWriteResponse {

  Long postId;

  String content;

  String name;

  LocalDateTime createdAt;

  LocalDateTime updatedAt;

  Long commentCount;

}
