package pr1.board.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pr1.board.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}