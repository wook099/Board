package pr1.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pr1.board.dto.PostRequestDto;
import pr1.board.dto.PostResponseDto;
import pr1.board.entity.Post;
import pr1.board.entity.User;
import pr1.board.repository.PostRepository;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public Long write(PostRequestDto dto,User user) {
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(user)
                .build();

        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

//    public List<PostResponseDto> getAllPosts() {
//        return postRepository.findAll().stream()
//                .map(post -> new PostResponseDto(
//                        post.getId(),
//                        post.getTitle(),
//                        post.getContent(),
//                        post.getAuthor().getUsername()//조회시 N+1문제 발생!
//                )).collect(Collectors.toList());
//    }
    //-------------
//    public PostResponseDto getPost(Long id) {
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
//        return PostResponseDto.builder()
//                .id(post.getId())
//                .title(post.getTitle())
//                .content(post.getContent())
//                .writer(post.getAuthor().getUsername())
//                .build();
//    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAllWithAuthor();

        return posts.stream()
                .map(p -> new PostResponseDto(
                        p.getId(),
                        p.getTitle(),
                        p.getContent(),
                        p.getAuthor().getUsername()
                ))
                .toList();
    }
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findByIdWithAuthor(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getUsername()
        );
    }

    @Transactional  // DB 변경 작업은 트랜잭션 필요
    public void updatePost(Long id, PostRequestDto dto, User user) throws AccessDeniedException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음"));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("작성자만 수정가능");
        }

        post.update(dto);  // 엔티티 내에서 변경 메서드 호출 (dirty checking으로 자동 반영됨)
    }

    public void deletePost(Long id,User user) throws AccessDeniedException{

        Post post = postRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글 없음"));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("작성자만 삭제가능");
        }

        if(post.isDeleted()) {
            throw new IllegalStateException("이미 삭제된 게시글입니다.");
        }

        //post.delete(); delete삭제변수 상태변경
        postRepository.delete(post);
    }

    public void purgeOldDeletedPosts() { // delete변수가 true인 경우에
        LocalDateTime DayAgo = LocalDateTime.now().minusDays(7);//7일 지난시점
        List<Post> oldDeletedPosts = postRepository.DeletedAtBefore(DayAgo);
        postRepository.deleteAll(oldDeletedPosts); // 일주일 뒤에 물리 삭제
    }
}