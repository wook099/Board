package pr1.board.entity;

import jakarta.persistence.*;
import lombok.*;
import pr1.board.dto.PostRequestDto;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author; // 이제 User 엔티티로 연관관계 - 작성자

    private boolean deleted = false;

    private LocalDateTime deletedAt; // 삭제 시간 기록

    @Transient//DB에 저장하지 않을 컬럼
    private Long Likecnt;

    public void update(PostRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

    public void delete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }


}
