package spring.hugme.domain.community.dto.request;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.hugme.global.code.BoardAlias;


@Data
@NoArgsConstructor
public class PostWriteRequest {

  private BoardAlias type;

  private String title;

  private String content;

  private List<String> imageURL;

  private List<String> hashTagContent;


}
