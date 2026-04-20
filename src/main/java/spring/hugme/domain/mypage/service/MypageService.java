package spring.hugme.domain.mypage.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.hugme.domain.community.dto.PostListProjection;
import spring.hugme.domain.community.dto.TagInfo;
import spring.hugme.domain.community.dto.response.BoardListResponse;
import spring.hugme.domain.community.entity.Comments;
import spring.hugme.domain.community.entity.Favorite;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.entity.PostImage;
import spring.hugme.domain.community.model.repo.CommentRepository;
import spring.hugme.domain.community.model.repo.FavoriteRepository;
import spring.hugme.domain.community.model.repo.PostImageRepository;
import spring.hugme.domain.community.model.repo.PostRepository;
import spring.hugme.domain.mypage.dto.MyCommentViewResponse;
import spring.hugme.domain.mypage.dto.MyInfoModifyRequest;
import spring.hugme.domain.mypage.dto.MyInfoResponse;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.domain.user.repository.UserRepository;
import spring.hugme.global.error.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class MypageService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final PostImageRepository postImageRepository;
  private final FavoriteRepository favoriteRepository;
  private final CommentRepository commentRepository;


  public List<BoardListResponse> PostList(Member member, List<Post> posts){
    return posts.stream()
        .map(post -> {
          List<TagInfo> tagInfoList = post.getHashtagList().stream()
              .map(hashtag -> TagInfo.builder()
                  .tagId(hashtag.getHashtagId())
                  .tagName(hashtag.getHashtagContent())
                  .build())
              .collect(Collectors.toList());

          PostListProjection counts = postRepository.findCountsByPostId(post.getPostId());

          Optional<PostImage> imageOpt = postImageRepository.findFirstByPostAndActivatedTrue(post);
          Favorite favorite = favoriteRepository.findByMemberAndPost(member, post);

          boolean liketrue = false;
          if (favorite != null) {
            liketrue = favorite.getActivated();
          }
          String imageUrl = imageOpt.map(PostImage::getSavePath)
              .orElse(
                  "https://res.cloudinary.com/dyz2lq1f0/image/upload/v1763707069/post_uploads/m1ryt6ptdy6wyydp4yog.png");

          return BoardListResponse.builder()
              .type(post.getBoard().getType())
              .userId(member.getId())
              .postId(post.getPostId())
              .boardId(post.getBoard().getBoardId())
              .nickname(member.getName())
              .title(post.getTitle())
              .content(post.getContent())
              .tag(tagInfoList)
              .likeCount(counts.getLikeCount())
              .commentCount(counts.getCommentCount())
              .imageUrl(imageUrl)
              .profileImageUrl(member.getProfileUrl())
              .updatedAt(post.getModifiedAt())
              .createdAt(post.getCreatedAt())
              .isLiked(liketrue)
              .build();
        })
        .collect(Collectors.toList());
  }
  public MyInfoResponse myInfoView(String userId) {

    Member member = userRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다"));

    return MyInfoResponse.builder()
        .userId(member.getUserId())
        .email(member.getEmail())
        .name(member.getName())
        .birthday(member.getBirthday())
        .phone(member.getPhone())
        .profileURL(member.getProfileUrl())
        .build();
  }

  @Transactional
  public void myInfoModify(MyInfoModifyRequest request, String userId) {

    Member member = userRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다"));

    member.setPhone(request.getPhone());
    member.setBirthday(request.getBirthday());
    member.setName(request.getName());
    member.setUserId(request.getUserId());
    member.setEmail(request.getEmail());


  }


  //다른것도 다 삭제 해야한다
  @Transactional
  public void myInfoDelete(String userId) {

    Member member = userRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다"));

    member.setActivated(false);
  }

  public List<BoardListResponse> myPostList(String userId) {

    Member member = userRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다"));

    List<Post> posts = postRepository.findAllMemberWithAllRelations(member);

    if(posts.isEmpty()){
      return null;
    }

    return PostList(member, posts);


  }

  // 내가 작성한 댓글 보기
  public List<MyCommentViewResponse> myCommentView(String userId) {

    Member member = userRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다"));

    List<Comments> comments = commentRepository.findByMember(member);

    return comments.stream()
        .map( comment -> {
          PostListProjection counts = postRepository.findCountsByPostId(comment.getPost().getPostId());

          return MyCommentViewResponse.builder()
              .postId(comment.getPost().getPostId())
              .title(comment.getPost().getTitle())
              .comment(comment.getContent())
              .commentCount(counts.getCommentCount())
              .LikeCount(counts.getLikeCount())
              .postCreatedAt(comment.getPost().getCreatedAt())
              .commentCreatedAt(comment.getCreatedAt())
              .postModifyAt(comment.getPost().getModifiedAt())
              .commentModifyAt(comment.getModifiedAt())
              .build();


            }
            ).toList();
  }

  @Transactional
  public List<BoardListResponse> myLikeView(String userId) {
    Member member = userRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다"));

    List<Post> posts = favoriteRepository.findLikedPostsByMember(member);
    if (posts == null || posts.isEmpty()) {
      return List.of();
    }

    return PostList(member, posts);
  }
}
