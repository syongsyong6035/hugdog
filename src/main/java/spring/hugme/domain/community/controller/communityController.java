package spring.hugme.domain.community.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.hugme.domain.user.repository.UserRepository;
import spring.hugme.global.code.BoardAlias;
import org.springframework.web.multipart.MultipartFile;
import spring.hugme.domain.community.dto.response.BoardListResponse;
import spring.hugme.domain.community.dto.response.PostDetailResponse;
import spring.hugme.domain.community.dto.request.PostWriteRequest;
import spring.hugme.domain.community.dto.response.PostWriteResponse;
import spring.hugme.domain.community.model.service.CommunityService;
import spring.hugme.global.controller.BaseController;
import spring.hugme.domain.image.service.ImageService;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.domain.user.service.MemberService;
import spring.hugme.global.response.CommonApiResponse;
import spring.hugme.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping(value =BaseController.API_V1 + "/community/posts", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class communityController {

  private final CommunityService communityService;
  private final ImageService imageService;
  private final MemberService memberService;
  private final UserRepository memberRepository;


  //전체 조회
  @GetMapping
  public CommonApiResponse<List<BoardListResponse>> BoardAllList(@RequestParam(required = false) BoardAlias type, Principal principal){

    List<BoardListResponse> boardList;

    Optional<Member> currentUserMember = Optional.empty();

    if (principal != null && !principal.getName().equals("anonymousUser")) {

      String userId = principal.getName();
      currentUserMember = memberRepository.findUserId(userId);
    }

    if (type != null) {
      // 쿼리 파라미터 'type'이 있을 경우 (예: /posts?type=QNA)
      boardList = communityService.BoardTypeAllList(type,currentUserMember);
    } else {
      // 쿼리 파라미터 'type'이 없을 경우 (예: /posts)
      boardList = communityService.BoardAllList(currentUserMember);
    }

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 커뮤니티 조회를 성공하였습니다.",
        boardList
        );

  }



  //글 상세
  @GetMapping("/detail/{postId}")
  public CommonApiResponse<PostDetailResponse> PostDetailView(@PathVariable Long postId, Principal principal){


    Optional<Member> currentUserMember = Optional.empty();

    if (principal != null && !principal.getName().equals("anonymousUser")) {

      String userId = principal.getName();
      currentUserMember = memberRepository.findUserId(userId);
    }

    PostDetailResponse postDetailInfo = communityService.PostDetailView(postId, currentUserMember);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 커뮤니티 상세조회를 성공하였습니다.",
        postDetailInfo
    );
  }
  //글작성시 이미지 보내주는거
  @PostMapping("/images")
  public CommonApiResponse<?> uploadImages(
      @RequestParam("images") List<MultipartFile> images) {
    // Service 에서 모든 유효성 검사 및 예외 처리를 담당
    // 여기서 예외가 발생하면 GlobalExceptionHandler 가 처리
    List<String> urls = imageService.upload(images);

    // 성공 응답은 ApiResponse 포맷으로 반환
    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 이미지가 생성됩니다",
        urls
    );
  }

  //글 작성
  @PostMapping("/detail")
  public CommonApiResponse<PostWriteResponse> PostWrite(@RequestBody final PostWriteRequest postWriteRequest, @AuthenticationPrincipal String userId){

    Member member = memberService.findByUserId(userId);

    PostWriteResponse response = communityService.PostWrite(member, postWriteRequest);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 커뮤니티 글이 생성되었습니다 ",
        response
    );

  }

  //글 수정
  @PatchMapping("/detail/{postId}")
  public CommonApiResponse<String> PostModify(@RequestBody final PostWriteRequest postWriteRequest,@AuthenticationPrincipal String userId, @PathVariable Long postId){

    communityService.PostModify(postWriteRequest, userId, postId);


    return CommonApiResponse.success(
        ResponseCode.NO_CONTENT,
        "정상적으로 커뮤니티글이 수정되었습니다."
    );

  }

  //글 삭제
  @DeleteMapping("/detail/{postId}")
  public CommonApiResponse<String> PostDelete(@PathVariable Long postId){

    communityService.PostDelete(postId);

    return CommonApiResponse.success(
        ResponseCode.NO_CONTENT,
        "정상적으로 커뮤니티 글이 삭제되었습니다."
    );

  }

  //글 상세보기에서 추천 글 목록
  @GetMapping("/{postId}/recommend")
  public CommonApiResponse<List<BoardListResponse>> RecommendPosts(@PathVariable Long postId, Principal principal){

    Optional<Member> currentUserMember = Optional.empty();

    if (principal != null && !principal.getName().equals("anonymousUser")) {

      String userId = principal.getName();
      currentUserMember = memberRepository.findUserId(userId);
    }

    List<BoardListResponse> responses = communityService.RecommendPosts(postId, currentUserMember);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 커뮤니티 추천글이 불러와졌습니다.",
        responses
    );

  }

}
