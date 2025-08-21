package pr1.board.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pr1.board.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시글 하나 조회 시 작성자까지 같이 가져오기
    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :id")
    Optional<Post> findByIdWithAuthor(@Param("id") Long id);

    // 여러 게시글 조회 시 작성자까지 같이 가져오기
    @Query("SELECT p FROM Post p JOIN FETCH p.author")
    List<Post> findAllWithAuthor();

    List<Post> DeletedAtBefore(LocalDateTime dateTime);

}