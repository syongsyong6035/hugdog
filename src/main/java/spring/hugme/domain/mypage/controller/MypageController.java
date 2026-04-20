package spring.hugme.domain.mypage.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;
import spring.hugme.domain.community.dto.response.BoardListResponse;
import spring.hugme.domain.mypage.dto.MyCommentViewResponse;
import spring.hugme.domain.mypage.dto.MyDogCreateRequest;
import spring.hugme.domain.mypage.dto.MyDogListResponse;
import spring.hugme.domain.mypage.dto.MyInfoModifyRequest;
import spring.hugme.domain.mypage.dto.MyInfoResponse;
import spring.hugme.domain.mypage.dto.MydogCreateResponse;
import spring.hugme.domain.mypage.service.MypageDogService;
import spring.hugme.domain.mypage.service.MypageService;
import spring.hugme.global.controller.BaseController;
import spring.hugme.global.response.CommonApiResponse;
import spring.hugme.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping(value =BaseController.API_V1 + "/mypage", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MypageController {

  private final MypageService mypageService;
  private final MypageDogService mypageDogService;

  //내 정보 보기
  @GetMapping
  public CommonApiResponse<MyInfoResponse> myInfoView(@AuthenticationPrincipal String userId){

    MyInfoResponse response = mypageService.myInfoView(userId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 회원 정보가 요청되었습니다.",
        response
    );

  }

  //내 정보 수정
  @PatchMapping
  public CommonApiResponse<String> myInfoModify(@RequestBody MyInfoModifyRequest request, @AuthenticationPrincipal String userId){

    mypageService.myInfoModify(request, userId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 회원의 정보가 수정되었습니다."
    );
  }

  //회원탈되
  @DeleteMapping
  public CommonApiResponse<String> myInfoDelete(@AuthenticationPrincipal String userId){

    mypageService.myInfoDelete(userId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 회원이 탈퇴되었습니다."
    );
  }

  //내가 작성한글 보기
  @GetMapping("/posts")
  public CommonApiResponse<List<BoardListResponse>> myPostList(@AuthenticationPrincipal String userId){

    List<BoardListResponse> response = mypageService.myPostList(userId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 작성한 글이 불러와졌습니다.",
        response
    );
  }

  @GetMapping("/comments")
  public CommonApiResponse<List<MyCommentViewResponse>> myCommentView(@AuthenticationPrincipal String userId){

    List<MyCommentViewResponse> response = mypageService.myCommentView(userId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 회원의 댓글이 불러와졌습니다.",
        response
    );

  }
  //내가 공감한 글
  @GetMapping("/likes")
  public CommonApiResponse<List<BoardListResponse>> myLikeView(@AuthenticationPrincipal String userId) {

    List<BoardListResponse> response = mypageService.myLikeView(userId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 회원이 공감한 글이 불러와졌습니다.",
        response
    );
  }


  //반려견 목록
  @GetMapping("/dogs")
  public CommonApiResponse<List<MyDogListResponse>> myDogList(@AuthenticationPrincipal String userId){

    List<MyDogListResponse> response = mypageDogService.myDogList(userId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 강아지 목록이 불러와졌습니다.",
        response
    );
  }

  //반려견 생성하기
  @PostMapping("/dogs")
  public CommonApiResponse<MydogCreateResponse> myDogCreate(@RequestBody MyDogCreateRequest request, @AuthenticationPrincipal String userId) {

    Long dogId = mypageDogService.myDogCreate(userId, request);

    MydogCreateResponse response = MydogCreateResponse.builder()
        .dogId(dogId)
        .build();
    return CommonApiResponse.success(
        ResponseCode.CREATED,
        "정상적으로 강아지가 등록되었습니다",
        response
    );
  }

  //반려견 수정하기
  @PatchMapping("/dogs/{dogId}")
  public CommonApiResponse<ResponseCode> myDogModify(@RequestBody MyDogCreateRequest request, @PathVariable Long dogId) {

     mypageDogService.myDogModify(dogId, request);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 강아지가 수정되었습니다.",
        ResponseCode.NO_CONTENT

    );
  }
  //반려견 삭제하기
  @DeleteMapping("/dogs/{dogId}")
  public CommonApiResponse<ResponseCode> myDogDelete(@PathVariable Long dogId) {

    mypageDogService.myDogDelete(dogId);

    return CommonApiResponse.success(
        ResponseCode.OK,
        "정상적으로 강아지가 삭제되었습니다.",
        ResponseCode.NO_CONTENT

    );
  }
  }
