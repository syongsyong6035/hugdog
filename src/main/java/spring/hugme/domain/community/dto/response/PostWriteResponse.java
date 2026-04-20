package spring.hugme.domain.community.dto.response;


import lombok.Builder;
import lombok.Data;
import spring.hugme.global.code.BoardAlias;


@Data
@Builder
public class PostWriteResponse {

  Long postId;

  Long boardId;

  BoardAlias type;

}
