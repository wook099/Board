package pr1.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pr1.board.dto.CommentRequestDto;
import pr1.board.dto.CommentResponseDto;
import pr1.board.entity.Comment;
import pr1.board.entity.Post;
import pr1.board.entity.User;
import pr1.board.repository.CommentRepository;
import pr1.board.repository.PostRepository;
import pr1.board.repository.UserRepository;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final SimpMessagingTemplate messagingTemplate;

    // 댓글 작성
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

        // WebSocket 알림 전송
        String notificationMsg = saved.getAuthor().getUsername() + "님이 댓글을 작성했습니다!";
        messagingTemplate.convertAndSend("/topic/notifications/" + post.getAuthor().getId(), notificationMsg);

        System.out.println("[알림 전송] " + notificationMsg + " / 수신자: " + post.getAuthor().getId());

        return CommentResponseDto.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .authorName(saved.getAuthor().getUsername())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // 특정 게시물에 대한 댓글 조회
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

    //댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long userId, Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("작성자만 수정 가능합니다.");
        }

        comment.setContent(content); // Comment값 변경
        return toDto(comment);
    }

    // 댓글 삭제 (논리 삭제)
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("작성자만 삭제 가능합니다.");
        }

        commentRepository.delete(comment);
    }


    private CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorName(comment.getAuthor().getUsername())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
