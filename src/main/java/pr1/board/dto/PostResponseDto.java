package pr1.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pr1.board.entity.Post;
@Getter
@AllArgsConstructor
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writer;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getAuthor().getUsername(); // writer 필드에 맞게 수정
    }
}
