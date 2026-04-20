package spring.hugme.domain.community.model.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.hugme.domain.community.dto.response.BoardListResponse;
import spring.hugme.domain.community.dto.response.PostDetailResponse;
import spring.hugme.domain.community.dto.request.PostWriteRequest;
import spring.hugme.domain.community.dto.response.PostWriteResponse;
import spring.hugme.domain.community.dto.PostListProjection;
import spring.hugme.domain.community.entity.Comments;
import spring.hugme.domain.community.entity.Favorite;
import spring.hugme.domain.community.model.repo.CommentRepository;
import spring.hugme.domain.community.model.repo.FavoriteRepository;
import spring.hugme.domain.user.repository.UserRepository;
import spring.hugme.global.code.BoardAlias;
import spring.hugme.domain.community.dto.TagInfo;
import spring.hugme.domain.community.entity.Board;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.entity.PostHashtag;
import spring.hugme.domain.community.entity.PostImage;
import spring.hugme.domain.community.model.repo.BoardRepository;
import spring.hugme.domain.community.model.repo.PostHashTagRepository;
import spring.hugme.domain.community.model.repo.PostImageRepository;
import spring.hugme.domain.community.model.repo.PostRepository;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.global.error.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityService {

  private final PostRepository postRepository;
  private final BoardRepository boardRepository;
  private final PostHashTagRepository postHashTagRepository;
  private final PostImageRepository postImageRepository;
  private final FavoriteRepository favoriteRepository;
  private final UserRepository memberRepository;
  private final CommentRepository commentRepository;

  @Value("${upload.path}")
  private String uploadPath;

  @PostConstruct
  public void init() {
    // src/main/resources/static/image 디렉토리 사용
    String projectRoot = System.getProperty("user.dir");
    this.uploadPath = projectRoot + File.separator + "src" + File.separator + "main" +
        File.separator + "resources" + File.separator + "static" +
        File.separator + "images";
    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) {
      uploadDir.mkdirs();
    }
    log.info("File upload path initialized to: {}", uploadPath);
  }

// 전체 글 보기
  public List<BoardListResponse> BoardAllList(Optional member) {

    List<Post> postList = postRepository.findAllWithAllRelations();

    return toBoardListResponse(postList, member);
  }


  //해당되는 타입 글 보기
  public List<BoardListResponse> BoardTypeAllList(BoardAlias type, Optional member) {

    Board board = boardRepository.findByType(type)
        .orElseThrow(() -> new NotFoundException("커뮤니티 타입 요청이 잘못되었습니다"));

    List<Post> postList = postRepository.findAllByBoardWithBoardAndMember(board);


    return toBoardListResponse(postList, member);
  }

  public PostDetailResponse PostDetailView(Long postId, Optional member) {

    Post post = postRepository.findByPostIdWithAllRelations(postId);

    if(post.getActivated() == false){

      throw new NotFoundException("해당 글이 존재하지 않습니다");
    }

    List<PostHashtag> postHashtags = postHashTagRepository.findAllByPost(post);


    List<TagInfo> tagInfos = postHashtags.stream()
        .map(tag ->
            TagInfo.builder()
                .tagId(tag.getHashtagId())
                .tagName(tag.getHashtagContent())
                .build()
        )
        .toList();

    PostListProjection counts = postRepository.findCountsByPostId(postId);
    boolean liketrue = false;

    if (member.isPresent()) {
      // 로그인된 사용자일 때만 좋아요 상태를 확인합니다.
      Member loginMember = (Member) member.get();

      Favorite favorite = favoriteRepository.findByMemberAndPost(loginMember, post);

      if (favorite != null) {
        liketrue = favorite.getActivated();
      }
    }

    List<PostImage> postImages = postImageRepository.findAllByPost(post);

    List<String> postImageUrl = postImages.stream()
        .map(postUrl -> postUrl.getSavePath())
        .toList();

    PostDetailResponse postDetailResponse = PostDetailResponse.builder()
        .userId(post.getMember().getId())
        .postId(postId)
        .boardId(post.getBoard().getBoardId())
        .type(post.getBoard().getType())
        .nickname(post.getMember().getName())
        .tag(tagInfos)
        .title(post.getTitle())
        .content(post.getContent())
        .commentCount(counts.getCommentCount())
        .likeCount(counts.getLikeCount())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getModifiedAt())
        .isLiked(liketrue)
        .imageUrl(postImageUrl)
        .profileImageUrl(post.getMember().getProfileUrl())
        .postUserId(post.getMember().getUserId())
        .build();

    return postDetailResponse;


  }

  private List<BoardListResponse> toBoardListResponse(List<Post> postList, Optional member) {
    return postList.stream()
        .map(post -> {

          List<TagInfo> tagInfoList = post.getHashtagList().stream()
              .map(hashtag -> TagInfo.builder()
                  .tagId(hashtag.getHashtagId())
                  .tagName(hashtag.getHashtagContent())
                  .build())
              .collect(Collectors.toList());

          PostListProjection counts = postRepository.findCountsByPostId(post.getPostId());

          Optional<PostImage> imageOpt = postImageRepository.findFirstByPostAndActivatedTrue(post);
          boolean liketrue = false;

          if (member.isPresent()) {
            // 로그인된 사용자일 때만 좋아요 상태를 확인합니다.
            Member loginMember = (Member) member.get();

            Favorite favorite = favoriteRepository.findByMemberAndPost(loginMember, post);

            if (favorite != null) {
              liketrue = favorite.getActivated();
            }
          }

          String imageUrl = imageOpt.map(PostImage::getSavePath)
              .orElse("https://res.cloudinary.com/dyz2lq1f0/image/upload/v1763707069/post_uploads/m1ryt6ptdy6wyydp4yog.png");
          return BoardListResponse.builder()
              .boardId(post.getBoard().getBoardId())
              .userId(post.getMember().getId())
              .nickname(post.getMember().getName())
              .postId(post.getPostId())
              .title(post.getTitle())
              .content(post.getContent())
              .commentCount(counts.getCommentCount())
              .type(post.getBoard().getType())
              .likeCount(counts.getLikeCount())
              .tag(tagInfoList)
              .imageUrl(imageUrl)
              .profileImageUrl(post.getMember().getProfileUrl())
              .createdAt(post.getCreatedAt())
              .updatedAt(post.getModifiedAt())
              .isLiked(liketrue)
              .build();
        })
        .collect(Collectors.toList());
  }

  @Transactional
  public PostWriteResponse PostWrite(Member member, PostWriteRequest request) {

    Board board = boardRepository.findByType(request.getType())
        .orElseThrow(() -> new NotFoundException("해당 게시판이 존재하지 않습니다"));

    Post post = Post.builder()
        .board(board)
        .member(member)
        .title(request.getTitle())
        .content(request.getContent())
        .build();

    postRepository.save(post);

    if(request.getHashTagContent() != null){
      for(String postHashTag : request.getHashTagContent()){
        PostHashtag postHashtag = PostHashtag.builder()
            .post(post)
            .hashtagContent(postHashTag)
            .build();
        postHashTagRepository.save(postHashtag);
      }

    }

    for (String imagePath : request.getImageURL()) {
      PostImage image = PostImage.builder()
          .post(post)
          .originFileName(extractFileName(imagePath))
          .renameFileName(extractFileName(imagePath))
          .savePath(imagePath)
          .build();
      postImageRepository.save(image);
    }
    PostWriteResponse response = PostWriteResponse.builder()
        .postId(post.getPostId())
        .boardId(board.getBoardId())
        .type(board.getType())
        .build();

    return response;

  }

  private String extractFileName(String path) {
    return path.substring(path.lastIndexOf('/') + 1);
  }

  @Transactional
  public void PostModify(PostWriteRequest request, String userId, Long postId) {

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("해당 게시물이 존재하지 않습니다"));



    postHashTagRepository.deleteAllByPost(post);

    postImageRepository.deleteAllByPost(post);

    post.setContent(request.getContent());
    post.setTitle(request.getTitle());

    if(request.getHashTagContent() != null){
      for(String postHashTag : request.getHashTagContent()){

        PostHashtag postHashtag = PostHashtag.builder()
            .post(post)
            .hashtagContent(postHashTag)
            .build();
        postHashTagRepository.save(postHashtag);
      }

    }

    for (String imagePath : request.getImageURL()) {
      PostImage image = PostImage.builder()
          .post(post)
          .originFileName(extractFileName(imagePath))
          .renameFileName(extractFileName(imagePath))
          .savePath(imagePath)
          .build();
      postImageRepository.save(image);
    }



  }

  @Transactional
  public void PostDelete(Long postId) {

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new NotFoundException("해당 글이 존재하지 않습니다"));

    List<Comments> comments = commentRepository.findByPost(post);

    List<PostImage> images = postImageRepository.findAllByPost(post);

    List<Favorite> favorites = favoriteRepository.findByPost(post);

    List<PostHashtag> hashtags = postHashTagRepository.findAllByPost(post);

    comments.forEach(comment -> comment.setActivated(false));
    images.forEach(image -> image.setActivated(false));
    favorites.forEach(favorite -> favorite.setActivated(false));
    hashtags.forEach(hashtag -> hashtag.setActivated(false));


    post.setActivated(false);
  }

  @Transactional
  public List<BoardListResponse> RecommendPosts(Long postId, Optional member) {

    Post post = postRepository.findById(postId)
        .orElseThrow(()-> new NotFoundException("해당 글이 존재하지 않습니다."));

    List<Post> posts = postRepository.findAllByRecommendPost(postId, post.getHashtagList(), 6);

    return toBoardListResponse(posts, member);

  }
}
