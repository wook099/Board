package pr1.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pr1.board.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);
}
