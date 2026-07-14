package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Post_Repo extends JpaRepository<Post, Long> {

    Page<Post> findAllByAuthorId(Long userId, Pageable pageable);



    @Query("SELECT p FROM Post p WHERE p.author.id = :currentUserId " +
            "OR p.author.id IN (SELECT f.user1.id FROM Friendship f WHERE f.user2.id = :currentUserId) " +
            "OR p.author.id IN (SELECT f.user2.id FROM Friendship f WHERE f.user1.id = :currentUserId) " +
            "ORDER BY p.createdDate DESC")
    Page<Post> findHomeFeed(@Param("currentUserId") Long currentUserId, Pageable pageable);

    boolean existsByAuthorId(Long id);

    Page<Post> findAllByAuthorIdIn(List<Long> ids ,  Pageable pageable);
}
