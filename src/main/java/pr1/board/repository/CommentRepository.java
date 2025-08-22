package pr1.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pr1.board.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
