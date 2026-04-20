package spring.hugme.domain.community.model.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.hugme.domain.community.dto.PostListProjection;
import spring.hugme.domain.community.dto.request.CommentWriteRequest;
import spring.hugme.domain.community.dto.response.CommentListResponse;
import spring.hugme.domain.community.dto.response.CommentWriteResponse;
import spring.hugme.domain.community.entity.Comments;
import spring.hugme.domain.community.entity.Favorite;
import spring.hugme.domain.community.entity.Post;
import spring.hugme.domain.community.model.repo.CommentRepository;
import spring.hugme.domain.community.model.repo.FavoriteRepository;
import spring.hugme.domain.community.model.repo.PostRepository;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.domain.user.repository.UserRepository;

import spring.hugme.global.error.exceptions.NotFoundException;


@Service
@RequiredArgsConstructor
public class ReactionService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository memberRepository;
  private final FavoriteRepository favoriteRepository;

  public List<CommentListResponse> CommentView(Long postId) {

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new NotFoundException("해당 게시물 아이디는 존재하지 않습니다."));

    List<Comments> commentsList = commentRepository.findAllCommentsWithMemberByPost(post);

    List<CommentListResponse> commentListResponses= commentsList.stream()
        .map( comment ->
            CommentListResponse.builder()
                .commentId(comment.getCommentId())
                .userId(comment.getMember().getUserId())
                .name(comment.getMember().getName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getModifiedAt())
                .profileImageUrl(comment.getMember().getProfileUrl())
                .build()
        )
        .toList();
    return  commentListResponses;
  }

  @Transactional
  public CommentWriteResponse CommentWrite(CommentWriteRequest commentRequest, Long postId, String userId) {

    Member member = memberRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다"));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new NotFoundException("해당 게시물이 존재하지 않습니다"));

    Comments comments = Comments.builder()
        .member(member)
        .post(post)
        .content(commentRequest.getContent())
        .build();

    comments = commentRepository.save(comments);


    PostListProjection counts = postRepository.findCountsByPostId(post.getPostId());


    return CommentWriteResponse.builder()
        .postId(postId)
        .content(comments.getContent())
        .name(member.getName())
        .createdAt(comments.getCreatedAt())
        .updatedAt(comments.getModifiedAt())
        .commentCount(counts.getCommentCount())
        .build();

  }

  @Transactional
  public void helpfullAdd(Long postId, String userId) {

    Member member = memberRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저는 존재하지 않습니다"));

    Post post = postRepository.findById(postId)
        .orElseThrow(()-> new NotFoundException("해당 게시글은 존재하지 않습니다"));

    Favorite favoriteActive = favoriteRepository.findByMemberAndPost(member, post);

    if(favoriteActive != null){

      favoriteActive.setActivated(true);

    }else{
      Favorite favorite = Favorite.builder()
          .post(post)
          .member(member)
          .magazine(null)
          .build();

      favoriteRepository.save(favorite);
    }


  }

  @Transactional
  public void helpfullDelete(Long postId, String userId) {


    Member member = memberRepository.findUserId(userId)
        .orElseThrow(() -> new NotFoundException("해당 유저는 존재하지 않습니다"));

    Post post = postRepository.findById(postId)
        .orElseThrow(()-> new NotFoundException("해당 게시글은 존재하지 않습니다"));

    Favorite favorite = favoriteRepository.findByMemberAndPost(member, post);

    if(favorite != null){

      favorite.setActivated(false);

    }else{
       throw new EntityNotFoundException("해당 게시물에 좋아요를 누르지 않아 취소할수 없습니다.");
    }

  }

  @Transactional
  public void CommentDelete(Long commentId, String userId) {

    Comments comments = commentRepository.findById(commentId)
        .orElseThrow(()-> new NotFoundException("해당 댓글이 존재하지 않습니다"));

    Member member = memberRepository.findUserId(userId)
        .orElseThrow(()-> new NotFoundException("로그인된 사용자는 존재하지 않습니다"));

    if(member != comments.getMember()){
      throw new IllegalArgumentException("로그인된 사용자와 댓글 쓴 사용자와 다릅니다.");
    }

    comments.setActivated(false);

  }
}
