package pr1.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String content;
    private Long postId; // 어느 게시글에 달리는 댓글인지
}