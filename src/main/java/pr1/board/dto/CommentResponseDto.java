package pr1.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentResponseDto {
    private Long id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
}
