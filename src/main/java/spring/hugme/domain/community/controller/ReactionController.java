package spring.hugme.domain.community.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.hugme.domain.community.dto.request.CommentWriteRequest;
import spring.hugme.domain.community.dto.response.CommentListResponse;
import spring.hugme.domain.community.dto.response.CommentWriteResponse;
import spring.hugme.domain.community.model.service.ReactionService;
import spring.hugme.global.controller.BaseController;
import spring.hugme.global.response.CommonApiResponse;
import spring.hugme.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.API_V1 + "/community/posts", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ReactionController {

  private final ReactionService reactionService;

  //댓글 목록
  @GetMapping("{postId}/comment")
  public CommonApiResponse<List<CommentListResponse>> CommentByPostIdList(
      @PathVariable Long postId) {

    List<CommentListResponse> commentListResponse = reactionService.CommentView(postId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 댓글 목록이 불러와졌습니다.",
        commentListResponse

    );

  }

  //댓글 작성
  @PostMapping("/{postId}/comment")
  public CommonApiResponse<CommentWriteResponse> CommentWrite(
      @RequestBody final CommentWriteRequest commentRequest, @AuthenticationPrincipal String userId,
      @PathVariable Long postId) {

    log.info(commentRequest.getContent());
    CommentWriteResponse response = reactionService.CommentWrite(commentRequest, postId, userId);

    return CommonApiResponse.success(
        ResponseCode.CREATED,
        "정상적으로 커뮤니티 댓글이 생성되었습니다",
        response
    );

  }

  //댓글 삭제
  @DeleteMapping("/{commentId}/comment")
  public CommonApiResponse<String> CommentDelete(
      @AuthenticationPrincipal String userId,
      @PathVariable Long commentId) {

     reactionService.CommentDelete(commentId, userId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 커뮤니티 댓글이 삭제되었습니다"
    );

  }

  //도움됩니다 추가
  @PostMapping("/{postId}/helpful")
  public CommonApiResponse<String> Addhelpful(@PathVariable Long postId,
      @AuthenticationPrincipal String userId) {

    reactionService.helpfullAdd(postId, userId);

    return CommonApiResponse.success(
        ResponseCode.CREATED,
        "정상적으로 도움됨이 추가되었습니다."
    );

  }


  //도움됩니다 취소
  @DeleteMapping("/{postId}/helpful")
  public CommonApiResponse<String> deletehelpful(@PathVariable Long postId,
      @AuthenticationPrincipal String userId) {

    reactionService.helpfullDelete(postId, userId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 도움됨이 삭제되었습니다."
    );

  }

}
