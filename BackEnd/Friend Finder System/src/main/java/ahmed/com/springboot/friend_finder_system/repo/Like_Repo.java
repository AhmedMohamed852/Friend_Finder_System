package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Like_Repo extends JpaRepository<Like, Long> {



    // boolean existsByUserIdAndPost_id(Long userId, Long postId);
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    void deleteByUserIdAndPostId(Long userId, Long postId);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
}
