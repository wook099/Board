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
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public Long write(PostRequestDto dto) {
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();

        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getWriter()
                )).collect(Collectors.toList());
    }

    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(post.getWriter())
                .build();
    }

    @Transactional  // DB 변경 작업이므로 트랜잭션 필요
    public void updatePost(Long id, PostRequestDto dto, User user) throws AccessDeniedException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음"));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("작성자만 수정가능");
        }

        post.update(dto);  // 엔티티 내에서 변경 메서드 호출 (dirty checking으로 자동 반영됨)
    }

}