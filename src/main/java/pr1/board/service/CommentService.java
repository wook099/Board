package pr1.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pr1.board.dto.CommentRequestDto;
import pr1.board.dto.CommentResponseDto;
import pr1.board.entity.Comment;
import pr1.board.entity.Post;
import pr1.board.entity.User;
import pr1.board.repository.CommentRepository;
import pr1.board.repository.PostRepository;
import pr1.board.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponseDto writeComment(Long userId, CommentRequestDto dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .post(post)
                .author(user)
                .build();

        Comment saved = commentRepository.save(comment);

        return CommentResponseDto.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .authorName(saved.getAuthor().getUsername())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public List<CommentResponseDto> getComments(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(c -> CommentResponseDto.builder()
                        .id(c.getId())
                        .content(c.getContent())
                        .authorName(c.getAuthor().getUsername())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList();
    }
}
