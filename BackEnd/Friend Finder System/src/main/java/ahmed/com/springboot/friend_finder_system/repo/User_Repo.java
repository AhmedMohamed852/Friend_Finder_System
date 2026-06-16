package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface User_Repo extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String username);
    boolean existsByPassword(String password);

    @Query("select u from User u join fetch u.roles where u.username = :username")
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
