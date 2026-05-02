package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface User_Repo extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String username);
    boolean existsByPassword(String password);


    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
